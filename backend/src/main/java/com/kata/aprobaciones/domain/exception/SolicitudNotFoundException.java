package com.kata.aprobaciones.domain.exception;

import java.util.UUID;

public class SolicitudNotFoundException extends DomainException {

    public SolicitudNotFoundException(UUID id) {
        super("No se encontro la solicitud con id: " + id);
    }
}
