package com.kata.aprobaciones.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kata.aprobaciones.domain.exception.NotificacionNotFoundException;
import com.kata.aprobaciones.domain.model.Notificacion;
import com.kata.aprobaciones.domain.port.in.MarcarNotificacionLeidaUseCase;
import com.kata.aprobaciones.domain.port.out.NotificacionPort;

@Service
public class MarcarNotificacionLeidaService implements MarcarNotificacionLeidaUseCase {

    private final NotificacionPort notificacionPort;

    public MarcarNotificacionLeidaService(NotificacionPort notificacionPort) {
        this.notificacionPort = notificacionPort;
    }

    @Override
    public Notificacion marcarLeida(UUID id) throws NotificacionNotFoundException {
        Notificacion notificacion = notificacionPort.buscarPorId(id)
                .orElseThrow(() -> new NotificacionNotFoundException(id));
        notificacion.marcarComoLeida();
        notificacionPort.actualizar(notificacion);
        return notificacion;
    }
}
