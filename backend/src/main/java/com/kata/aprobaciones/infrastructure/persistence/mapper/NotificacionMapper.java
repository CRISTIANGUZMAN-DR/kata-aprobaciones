package com.kata.aprobaciones.infrastructure.persistence.mapper;

import com.kata.aprobaciones.domain.model.Notificacion;
import com.kata.aprobaciones.infrastructure.persistence.entity.NotificacionEntity;

public final class NotificacionMapper {

    private NotificacionMapper() {
    }

    public static NotificacionEntity toEntity(Notificacion notificacion) {
        return new NotificacionEntity(
                notificacion.getId(),
                notificacion.getDestinatario(),
                notificacion.getSolicitudId(),
                notificacion.getMensaje(),
                notificacion.isLeida(),
                notificacion.getCreadoEn());
    }

    public static Notificacion toDomain(NotificacionEntity entity) {
        return Notificacion.reconstruir(
                entity.getId(),
                entity.getDestinatario(),
                entity.getSolicitudId(),
                entity.getMensaje(),
                entity.isLeida(),
                entity.getCreadoEn());
    }
}
