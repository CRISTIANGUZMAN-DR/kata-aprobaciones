package com.kata.aprobaciones.domain.port.in;

import com.kata.aprobaciones.domain.exception.AccionNoPermitidaException;
import com.kata.aprobaciones.domain.exception.SolicitudNotFoundException;
import com.kata.aprobaciones.domain.model.Solicitud;

public interface AprobarSolicitudUseCase {

    Solicitud aprobar(DecidirSolicitudCommand command) throws SolicitudNotFoundException, AccionNoPermitidaException;
}
