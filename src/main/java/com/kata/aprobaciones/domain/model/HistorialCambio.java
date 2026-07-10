package com.kata.aprobaciones.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class HistorialCambio {

    private final UUID id;
    private final UUID solicitudId;
    private final EstadoSolicitud estado;
    private final String comentario;
    private final String realizadoPor;
    private final LocalDateTime fecha;

    private HistorialCambio(UUID id, UUID solicitudId, EstadoSolicitud estado, String comentario,
            String realizadoPor, LocalDateTime fecha) {
        this.id = id;
        this.solicitudId = solicitudId;
        this.estado = estado;
        this.comentario = comentario;
        this.realizadoPor = realizadoPor;
        this.fecha = fecha;
    }

    public static HistorialCambio registrar(UUID solicitudId, EstadoSolicitud estado, String comentario,
            String realizadoPor) {
        return new HistorialCambio(UUID.randomUUID(), solicitudId, estado, comentario, realizadoPor,
                LocalDateTime.now());
    }

    public static HistorialCambio reconstruir(UUID id, UUID solicitudId, EstadoSolicitud estado, String comentario,
            String realizadoPor, LocalDateTime fecha) {
        return new HistorialCambio(id, solicitudId, estado, comentario, realizadoPor, fecha);
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
