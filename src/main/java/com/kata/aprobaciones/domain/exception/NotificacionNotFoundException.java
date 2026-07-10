package com.kata.aprobaciones.domain.exception;

import java.util.UUID;

public class NotificacionNotFoundException extends DomainException {

    public NotificacionNotFoundException(UUID id) {
        super("No se encontro la notificacion con id: " + id);
    }
}
