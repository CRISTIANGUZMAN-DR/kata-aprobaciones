package com.kata.aprobaciones.domain.port.in;

import java.util.UUID;

import com.kata.aprobaciones.domain.exception.NotificacionNotFoundException;
import com.kata.aprobaciones.domain.model.Notificacion;

public interface MarcarNotificacionLeidaUseCase {

    Notificacion marcarLeida(UUID id) throws NotificacionNotFoundException;
}
