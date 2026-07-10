package com.kata.aprobaciones.infrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.kata.aprobaciones.domain.model.EstadoSolicitud;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "historial_cambios")
public class HistorialCambioEntity {

    @Id
    private UUID id;

    @Column(name = "solicitud_id", nullable = false)
    private UUID solicitudId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado;

    private String comentario;

    @Column(name = "realizado_por", nullable = false)
    private String realizadoPor;

    @Column(nullable = false)
    private LocalDateTime fecha;

    protected HistorialCambioEntity() {
    }

    public HistorialCambioEntity(UUID id, UUID solicitudId, EstadoSolicitud estado, String comentario,
            String realizadoPor, LocalDateTime fecha) {
        this.id = id;
        this.solicitudId = solicitudId;
        this.estado = estado;
        this.comentario = comentario;
        this.realizadoPor = realizadoPor;
        this.fecha = fecha;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSolicitudId() {
        return solicitudId;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public String getComentario() {
        return comentario;
    }

    public String getRealizadoPor() {
        return realizadoPor;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
