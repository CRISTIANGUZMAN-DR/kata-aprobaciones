package com.kata.aprobaciones.infrastructure.persistence.mapper;

import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.infrastructure.persistence.entity.SolicitudEntity;

public final class SolicitudMapper {

    private SolicitudMapper() {
    }

    public static SolicitudEntity toEntity(Solicitud solicitud) {
        return new SolicitudEntity(
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

    public static Solicitud toDomain(SolicitudEntity entity) {
        return Solicitud.reconstruir(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getSolicitante(),
                entity.getAprobador(),
                entity.getTipo(),
                entity.getEstado(),
                entity.getCreadoEn(),
                entity.getActualizadoEn());
    }
}
