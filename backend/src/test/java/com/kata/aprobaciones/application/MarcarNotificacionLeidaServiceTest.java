package com.kata.aprobaciones.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kata.aprobaciones.domain.exception.NotificacionNotFoundException;
import com.kata.aprobaciones.domain.model.Notificacion;
import com.kata.aprobaciones.domain.port.out.NotificacionPort;

@ExtendWith(MockitoExtension.class)
class MarcarNotificacionLeidaServiceTest {

    @Mock
    private NotificacionPort notificacionPort;

    @Test
    void should_marcarComoLeida_when_notificacionExiste() throws NotificacionNotFoundException {
        MarcarNotificacionLeidaService service = new MarcarNotificacionLeidaService(notificacionPort);

        UUID id = UUID.randomUUID();
        Notificacion notificacion = Notificacion.crear("mgarcia", UUID.randomUUID(), "Tienes una solicitud pendiente");
        when(notificacionPort.buscarPorId(id)).thenReturn(Optional.of(notificacion));

        Notificacion resultado = service.marcarLeida(id);

        assertThat(resultado.isLeida()).isTrue();
        verify(notificacionPort).actualizar(notificacion);
    }

    @Test
    void should_lanzarExcepcion_when_notificacionNoExiste() {
        MarcarNotificacionLeidaService service = new MarcarNotificacionLeidaService(notificacionPort);

        UUID id = UUID.randomUUID();
        when(notificacionPort.buscarPorId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.marcarLeida(id))
                .isInstanceOf(NotificacionNotFoundException.class);
    }
}
