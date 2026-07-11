package com.kata.aprobaciones.infrastructure.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.kata.aprobaciones.domain.exception.NotificacionNotFoundException;
import com.kata.aprobaciones.domain.model.Notificacion;
import com.kata.aprobaciones.domain.port.in.ListarNotificacionesUseCase;
import com.kata.aprobaciones.domain.port.in.MarcarNotificacionLeidaUseCase;
import com.kata.aprobaciones.infrastructure.web.controller.NotificacionController;

@WebMvcTest(NotificacionController.class)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListarNotificacionesUseCase listarNotificacionesUseCase;

    @MockBean
    private MarcarNotificacionLeidaUseCase marcarNotificacionLeidaUseCase;

    @Test
    void should_retornar200ConLista_when_seConsultaBandeja() throws Exception {
        Notificacion notificacion = Notificacion.crear("mgarcia", UUID.randomUUID(), "Tienes una solicitud pendiente");
        when(listarNotificacionesUseCase.listar("mgarcia")).thenReturn(List.of(notificacion));

        mockMvc.perform(get("/api/notificaciones/{usuario}", "mgarcia")
                        .header("X-Usuario", "mgarcia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].destinatario").value("mgarcia"))
                .andExpect(jsonPath("$[0].leida").value(false));
    }

    @Test
    void should_retornar200_when_seMarcaComoLeida() throws Exception {
        UUID id = UUID.randomUUID();
        Notificacion notificacion = Notificacion.crear("mgarcia", UUID.randomUUID(), "Tienes una solicitud pendiente");
        notificacion.marcarComoLeida();
        when(marcarNotificacionLeidaUseCase.marcarLeida(id)).thenReturn(notificacion);

        mockMvc.perform(patch("/api/notificaciones/{id}/leer", id)
                        .header("X-Usuario", "mgarcia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.leida").value(true));
    }

    @Test
    void should_retornar404_when_notificacionNoExiste() throws Exception {
        UUID id = UUID.randomUUID();
        when(marcarNotificacionLeidaUseCase.marcarLeida(id)).thenThrow(new NotificacionNotFoundException(id));

        mockMvc.perform(patch("/api/notificaciones/{id}/leer", id)
                        .header("X-Usuario", "mgarcia"))
                .andExpect(status().isNotFound());
    }
}
