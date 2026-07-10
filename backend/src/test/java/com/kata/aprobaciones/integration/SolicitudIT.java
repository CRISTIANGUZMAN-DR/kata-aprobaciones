package com.kata.aprobaciones.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kata.aprobaciones.domain.model.TipoSolicitud;
import com.kata.aprobaciones.infrastructure.persistence.entity.HistorialCambioEntity;
import com.kata.aprobaciones.infrastructure.persistence.entity.NotificacionEntity;
import com.kata.aprobaciones.infrastructure.persistence.entity.SolicitudEntity;
import com.kata.aprobaciones.infrastructure.persistence.repository.HistorialCambioJpaRepository;
import com.kata.aprobaciones.infrastructure.persistence.repository.NotificacionJpaRepository;
import com.kata.aprobaciones.infrastructure.persistence.repository.SolicitudJpaRepository;
import com.kata.aprobaciones.infrastructure.web.dto.CrearSolicitudRequest;
import com.kata.aprobaciones.infrastructure.web.dto.SolicitudResponse;

@SpringBootTest
@AutoConfigureMockMvc
class SolicitudIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SolicitudJpaRepository solicitudJpaRepository;

    @Autowired
    private HistorialCambioJpaRepository historialCambioJpaRepository;

    @Autowired
    private NotificacionJpaRepository notificacionJpaRepository;

    @Test
    void should_crearSolicitudConHistorialYNotificacion_when_flujoCompleto() throws Exception {
        CrearSolicitudRequest request = new CrearSolicitudRequest(
                "Nueva version microservicio",
                "Aprobar despliegue de la version 2.3.0",
                "jperez",
                "mgarcia",
                TipoSolicitud.DESPLIEGUE);

        MvcResult result = mockMvc.perform(post("/api/solicitudes")
                        .header("X-Usuario", "jperez")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        SolicitudResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), SolicitudResponse.class);

        SolicitudEntity solicitudEntity = solicitudJpaRepository.findById(response.id()).orElseThrow();
        assertThat(solicitudEntity.getEstado().name()).isEqualTo("PENDIENTE");

        List<HistorialCambioEntity> historial = historialCambioJpaRepository.findAll();
        assertThat(historial).anyMatch(h -> h.getSolicitudId().equals(response.id()));

        List<NotificacionEntity> notificaciones = notificacionJpaRepository.findAll();
        assertThat(notificaciones).anyMatch(n -> n.getSolicitudId().equals(response.id())
                && n.getDestinatario().equals("mgarcia"));
    }
}
