package com.kata.aprobaciones.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.HistorialCambio;

public record HistorialCambioResponse(
        UUID id,
        EstadoSolicitud estado,
        String comentario,
        String realizadoPor,
        LocalDateTime fecha) {

    public static HistorialCambioResponse desde(HistorialCambio historialCambio) {
        return new HistorialCambioResponse(
                historialCambio.getId(),
                historialCambio.getEstado(),
                historialCambio.getComentario(),
                historialCambio.getRealizadoPor(),
                historialCambio.getFecha());
    }
}
