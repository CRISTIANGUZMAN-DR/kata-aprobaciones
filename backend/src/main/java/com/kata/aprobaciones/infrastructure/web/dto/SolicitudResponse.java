package com.kata.aprobaciones.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.model.TipoSolicitud;

public record SolicitudResponse(
        UUID id,
        String titulo,
        String descripcion,
        String solicitante,
        String aprobador,
        TipoSolicitud tipo,
        EstadoSolicitud estado,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn) {

    public static SolicitudResponse desde(Solicitud solicitud) {
        return new SolicitudResponse(
                solicitud.getId(),
                solicitud.getTitulo(),
                solicitud.getDescripcion(),
                solicitud.getSolicitante(),
                solicitud.getAprobador(),
                solicitud.getTipo(),
                solicitud.getEstado(),
                solicitud.getCreadoEn(),
                solicitud.getActualizadoEn());
    }
}
