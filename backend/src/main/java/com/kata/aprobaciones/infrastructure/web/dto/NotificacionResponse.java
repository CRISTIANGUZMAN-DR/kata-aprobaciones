package com.kata.aprobaciones.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.kata.aprobaciones.domain.model.Notificacion;

public record NotificacionResponse(
        UUID id,
        String destinatario,
        UUID solicitudId,
        String mensaje,
        boolean leida,
        LocalDateTime creadoEn) {

    public static NotificacionResponse desde(Notificacion notificacion) {
        return new NotificacionResponse(
                notificacion.getId(),
                notificacion.getDestinatario(),
                notificacion.getSolicitudId(),
                notificacion.getMensaje(),
                notificacion.isLeida(),
                notificacion.getCreadoEn());
    }
}
