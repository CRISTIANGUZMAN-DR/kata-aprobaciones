package com.kata.aprobaciones.infrastructure.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kata.aprobaciones.domain.exception.AccionNoPermitidaException;
import com.kata.aprobaciones.domain.exception.SolicitanteIgualAprobadorException;
import com.kata.aprobaciones.domain.exception.SolicitudNotFoundException;
import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.HistorialCambio;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.model.TipoSolicitud;
import com.kata.aprobaciones.domain.port.in.AprobarSolicitudUseCase;
import com.kata.aprobaciones.domain.port.in.CreateSolicitudUseCase;
import com.kata.aprobaciones.domain.port.in.GetSolicitudUseCase;
import com.kata.aprobaciones.domain.port.in.ListarSolicitudesUseCase;
import com.kata.aprobaciones.domain.port.in.RechazarSolicitudUseCase;
import com.kata.aprobaciones.domain.port.in.SolicitudDetalle;
import com.kata.aprobaciones.infrastructure.web.controller.SolicitudController;
import com.kata.aprobaciones.infrastructure.web.dto.CrearSolicitudRequest;
import com.kata.aprobaciones.infrastructure.web.dto.DecidirSolicitudRequest;

@WebMvcTest(SolicitudController.class)
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateSolicitudUseCase createSolicitudUseCase;

    @MockBean
    private AprobarSolicitudUseCase aprobarSolicitudUseCase;

    @MockBean
    private RechazarSolicitudUseCase rechazarSolicitudUseCase;

    @MockBean
    private ListarSolicitudesUseCase listarSolicitudesUseCase;

    @MockBean
    private GetSolicitudUseCase getSolicitudUseCase;

    private Solicitud solicitudPendiente(UUID id) {
        return Solicitud.reconstruir(
                id, "Nueva version microservicio", "Aprobar despliegue de la version 2.3.0",
                "jperez", "mgarcia", TipoSolicitud.DESPLIEGUE, EstadoSolicitud.PENDIENTE,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void should_retornar201_when_datosValidos() throws Exception {
        CrearSolicitudRequest request = new CrearSolicitudRequest(
                "Nueva version microservicio",
                "Aprobar despliegue de la version 2.3.0",
                "jperez",
                "mgarcia",
                TipoSolicitud.DESPLIEGUE);

        when(createSolicitudUseCase.crear(any())).thenReturn(solicitudPendiente(UUID.randomUUID()));

        mockMvc.perform(post("/api/solicitudes")
                        .header("X-Usuario", "jperez")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.solicitante").value("jperez"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void should_retornar400_when_faltaCampoRequerido() throws Exception {
        String bodyInvalido = """
                {
                  "titulo": "",
                  "descripcion": "Aprobar despliegue",
                  "solicitante": "jperez",
                  "aprobador": "mgarcia",
                  "tipo": "DESPLIEGUE"
                }
                """;

        mockMvc.perform(post("/api/solicitudes")
                        .header("X-Usuario", "jperez")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_retornar400_when_faltaHeaderXUsuario() throws Exception {
        CrearSolicitudRequest request = new CrearSolicitudRequest(
                "Nueva version microservicio",
                "Aprobar despliegue de la version 2.3.0",
                "jperez",
                "mgarcia",
                TipoSolicitud.DESPLIEGUE);

        mockMvc.perform(post("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_retornar409_when_solicitanteIgualAprobador() throws Exception {
        CrearSolicitudRequest request = new CrearSolicitudRequest(
                "Nueva version microservicio",
                "Aprobar despliegue de la version 2.3.0",
                "jperez",
                "jperez",
                TipoSolicitud.DESPLIEGUE);

        when(createSolicitudUseCase.crear(any())).thenThrow(new SolicitanteIgualAprobadorException("jperez"));

        mockMvc.perform(post("/api/solicitudes")
                        .header("X-Usuario", "jperez")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void should_retornar200_when_aprobarSolicitudValida() throws Exception {
        UUID id = UUID.randomUUID();
        Solicitud aprobada = solicitudPendiente(id);
        aprobada.aprobar("mgarcia");

        when(aprobarSolicitudUseCase.aprobar(any())).thenReturn(aprobada);

        DecidirSolicitudRequest request = new DecidirSolicitudRequest("mgarcia", "Todo en orden");

        mockMvc.perform(patch("/api/solicitudes/{id}/aprobar", id)
                        .header("X-Usuario", "mgarcia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADO"));
    }

    @Test
    void should_retornar404_when_solicitudNoExiste() throws Exception {
        UUID id = UUID.randomUUID();
        when(aprobarSolicitudUseCase.aprobar(any())).thenThrow(new SolicitudNotFoundException(id));

        DecidirSolicitudRequest request = new DecidirSolicitudRequest("mgarcia", null);

        mockMvc.perform(patch("/api/solicitudes/{id}/aprobar", id)
                        .header("X-Usuario", "mgarcia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_retornar409_when_usuarioNoEsElAprobador() throws Exception {
        UUID id = UUID.randomUUID();
        when(rechazarSolicitudUseCase.rechazar(any()))
                .thenThrow(new AccionNoPermitidaException("El usuario alopez no es el aprobador asignado"));

        DecidirSolicitudRequest request = new DecidirSolicitudRequest("alopez", "No corresponde");

        mockMvc.perform(patch("/api/solicitudes/{id}/rechazar", id)
                        .header("X-Usuario", "alopez")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void should_retornar200ConLista_when_seListanSolicitudes() throws Exception {
        when(listarSolicitudesUseCase.listar(eq("mgarcia"), isNull()))
                .thenReturn(List.of(solicitudPendiente(UUID.randomUUID())));

        mockMvc.perform(get("/api/solicitudes")
                        .header("X-Usuario", "mgarcia")
                        .param("aprobador", "mgarcia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].aprobador").value("mgarcia"));
    }

    @Test
    void should_retornar200ConDetalle_when_solicitudExiste() throws Exception {
        UUID id = UUID.randomUUID();
        Solicitud solicitud = solicitudPendiente(id);
        HistorialCambio historial = HistorialCambio.registrar(id, EstadoSolicitud.PENDIENTE, null, "jperez");

        when(getSolicitudUseCase.obtener(id)).thenReturn(new SolicitudDetalle(solicitud, List.of(historial)));

        mockMvc.perform(get("/api/solicitudes/{id}", id)
                        .header("X-Usuario", "mgarcia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.historial", org.hamcrest.Matchers.hasSize(1)));
    }

    @Test
    void should_retornar404_when_seConsultaSolicitudInexistente() throws Exception {
        UUID id = UUID.randomUUID();
        when(getSolicitudUseCase.obtener(id)).thenThrow(new SolicitudNotFoundException(id));

        mockMvc.perform(get("/api/solicitudes/{id}", id)
                        .header("X-Usuario", "mgarcia"))
                .andExpect(status().isNotFound());
    }
}
