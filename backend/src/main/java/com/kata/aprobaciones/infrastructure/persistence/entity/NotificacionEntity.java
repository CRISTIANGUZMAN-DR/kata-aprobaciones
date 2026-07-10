package com.kata.aprobaciones.infrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notificaciones")
public class NotificacionEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String destinatario;

    @Column(name = "solicitud_id", nullable = false)
    private UUID solicitudId;

    @Column(nullable = false)
    private String mensaje;

    @Column(nullable = false)
    private boolean leida;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    protected NotificacionEntity() {
    }

    public NotificacionEntity(UUID id, String destinatario, UUID solicitudId, String mensaje, boolean leida,
            LocalDateTime creadoEn) {
        this.id = id;
        this.destinatario = destinatario;
        this.solicitudId = solicitudId;
        this.mensaje = mensaje;
        this.leida = leida;
        this.creadoEn = creadoEn;
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
