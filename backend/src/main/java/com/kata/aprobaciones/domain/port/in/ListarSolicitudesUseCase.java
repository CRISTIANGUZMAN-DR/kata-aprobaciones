package com.kata.aprobaciones.domain.port.in;

import java.util.List;

import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.Solicitud;

public interface ListarSolicitudesUseCase {

    List<Solicitud> listar(String aprobador, EstadoSolicitud estado);
}
