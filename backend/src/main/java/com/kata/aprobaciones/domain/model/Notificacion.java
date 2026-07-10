package com.kata.aprobaciones.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notificacion {

    private final UUID id;
    private final String destinatario;
    private final UUID solicitudId;
    private final String mensaje;
    private boolean leida;
    private final LocalDateTime creadoEn;

    private Notificacion(UUID id, String destinatario, UUID solicitudId, String mensaje, boolean leida,
            LocalDateTime creadoEn) {
        this.id = id;
        this.destinatario = destinatario;
        this.solicitudId = solicitudId;
        this.mensaje = mensaje;
        this.leida = leida;
        this.creadoEn = creadoEn;
    }

    public static Notificacion crear(String destinatario, UUID solicitudId, String mensaje) {
        return new Notificacion(UUID.randomUUID(), destinatario, solicitudId, mensaje, false, LocalDateTime.now());
    }

    public static Notificacion reconstruir(UUID id, String destinatario, UUID solicitudId, String mensaje,
            boolean leida, LocalDateTime creadoEn) {
        return new Notificacion(id, destinatario, solicitudId, mensaje, leida, creadoEn);
    }

    public void marcarComoLeida() {
        this.leida = true;
    }

    public UUID getId() {
        return id;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public UUID getSolicitudId() {
        return solicitudId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean isLeida() {
        return leida;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }
}
