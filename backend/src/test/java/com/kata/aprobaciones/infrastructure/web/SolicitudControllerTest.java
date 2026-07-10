package com.kata.aprobaciones.infrastructure.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kata.aprobaciones.domain.exception.SolicitanteIgualAprobadorException;
import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.model.TipoSolicitud;
import com.kata.aprobaciones.domain.port.in.CreateSolicitudUseCase;
import com.kata.aprobaciones.infrastructure.web.controller.SolicitudController;
import com.kata.aprobaciones.infrastructure.web.dto.CrearSolicitudRequest;

@WebMvcTest(SolicitudController.class)
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateSolicitudUseCase createSolicitudUseCase;

    @Test
    void should_retornar201_when_datosValidos() throws Exception {
        CrearSolicitudRequest request = new CrearSolicitudRequest(
                "Nueva version microservicio",
                "Aprobar despliegue de la version 2.3.0",
                "jperez",
                "mgarcia",
                TipoSolicitud.DESPLIEGUE);

        Solicitud solicitud = Solicitud.reconstruir(
                UUID.randomUUID(),
                request.titulo(),
                request.descripcion(),
                request.solicitante(),
                request.aprobador(),
                request.tipo(),
                EstadoSolicitud.PENDIENTE,
                LocalDateTime.now(),
                LocalDateTime.now());

        when(createSolicitudUseCase.crear(any())).thenReturn(solicitud);

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
}
