package com.kata.aprobaciones.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.TipoSolicitud;
import com.kata.aprobaciones.domain.port.in.SolicitudDetalle;

public record SolicitudDetalleResponse(
        UUID id,
        String titulo,
        String descripcion,
        String solicitante,
        String aprobador,
        TipoSolicitud tipo,
        EstadoSolicitud estado,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn,
        List<HistorialCambioResponse> historial) {

    public static SolicitudDetalleResponse desde(SolicitudDetalle detalle) {
        var solicitud = detalle.solicitud();
        return new SolicitudDetalleResponse(
                solicitud.getId(),
                solicitud.getTitulo(),
                solicitud.getDescripcion(),
                solicitud.getSolicitante(),
                solicitud.getAprobador(),
                solicitud.getTipo(),
                solicitud.getEstado(),
                solicitud.getCreadoEn(),
                solicitud.getActualizadoEn(),
                detalle.historial().stream().map(HistorialCambioResponse::desde).toList());
    }
}
