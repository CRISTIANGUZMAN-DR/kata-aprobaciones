package com.kata.aprobaciones.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kata.aprobaciones.domain.model.Notificacion;
import com.kata.aprobaciones.domain.port.out.NotificacionPort;

@ExtendWith(MockitoExtension.class)
class ListarNotificacionesServiceTest {

    @Mock
    private NotificacionPort notificacionPort;

    @Test
    void should_delegarAlPuerto_when_seListanNotificaciones() {
        ListarNotificacionesService service = new ListarNotificacionesService(notificacionPort);

        Notificacion notificacion = Notificacion.crear("mgarcia", UUID.randomUUID(), "Tienes una solicitud pendiente");
        when(notificacionPort.listarPorDestinatario("mgarcia")).thenReturn(List.of(notificacion));

        List<Notificacion> resultado = service.listar("mgarcia");

        assertThat(resultado).containsExactly(notificacion);
    }
}
