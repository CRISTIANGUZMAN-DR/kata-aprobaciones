package com.kata.aprobaciones.domain.port.in;

import java.util.UUID;

import com.kata.aprobaciones.domain.exception.SolicitudNotFoundException;

public interface GetSolicitudUseCase {

    SolicitudDetalle obtener(UUID id) throws SolicitudNotFoundException;
}
