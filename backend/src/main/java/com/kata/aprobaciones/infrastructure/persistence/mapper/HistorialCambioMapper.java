package com.kata.aprobaciones.infrastructure.persistence.mapper;

import com.kata.aprobaciones.domain.model.HistorialCambio;
import com.kata.aprobaciones.infrastructure.persistence.entity.HistorialCambioEntity;

public final class HistorialCambioMapper {

    private HistorialCambioMapper() {
    }

    public static HistorialCambioEntity toEntity(HistorialCambio historialCambio) {
        return new HistorialCambioEntity(
                historialCambio.getId(),
                historialCambio.getSolicitudId(),
                historialCambio.getEstado(),
                historialCambio.getComentario(),
                historialCambio.getRealizadoPor(),
                historialCambio.getFecha());
    }

    public static HistorialCambio toDomain(HistorialCambioEntity entity) {
        return HistorialCambio.reconstruir(
                entity.getId(),
                entity.getSolicitudId(),
                entity.getEstado(),
                entity.getComentario(),
                entity.getRealizadoPor(),
                entity.getFecha());
    }
}
