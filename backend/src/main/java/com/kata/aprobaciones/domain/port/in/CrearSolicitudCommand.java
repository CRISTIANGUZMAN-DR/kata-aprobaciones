package com.kata.aprobaciones.domain.port.in;

import com.kata.aprobaciones.domain.model.TipoSolicitud;

public record CrearSolicitudCommand(
        String titulo,
        String descripcion,
        String solicitante,
        String aprobador,
        TipoSolicitud tipo) {
}
