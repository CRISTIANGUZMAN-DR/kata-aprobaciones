package com.kata.aprobaciones.domain.port.in;

import com.kata.aprobaciones.domain.exception.SolicitanteIgualAprobadorException;
import com.kata.aprobaciones.domain.model.Solicitud;

public interface CreateSolicitudUseCase {

    Solicitud crear(CrearSolicitudCommand command) throws SolicitanteIgualAprobadorException;
}
