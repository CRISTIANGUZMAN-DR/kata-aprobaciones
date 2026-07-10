package com.kata.aprobaciones.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.kata.aprobaciones.domain.exception.SolicitanteIgualAprobadorException;

public class Solicitud {

    private final UUID id;
    private final String titulo;
    private final String descripcion;
    private final String solicitante;
    private final String aprobador;
    private final TipoSolicitud tipo;
    private final EstadoSolicitud estado;
    private final LocalDateTime creadoEn;
    private final LocalDateTime actualizadoEn;

    private Solicitud(UUID id, String titulo, String descripcion, String solicitante, String aprobador,
            TipoSolicitud tipo, EstadoSolicitud estado, LocalDateTime creadoEn, LocalDateTime actualizadoEn) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.solicitante = solicitante;
        this.aprobador = aprobador;
        this.tipo = tipo;
        this.estado = estado;
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
    }

    public static Solicitud crear(String titulo, String descripcion, String solicitante, String aprobador,
            TipoSolicitud tipo) throws SolicitanteIgualAprobadorException {
        if (solicitante.equalsIgnoreCase(aprobador)) {
            throw new SolicitanteIgualAprobadorException(solicitante);
        }
        LocalDateTime ahora = LocalDateTime.now();
        return new Solicitud(UUID.randomUUID(), titulo, descripcion, solicitante, aprobador, tipo,
                EstadoSolicitud.PENDIENTE, ahora, ahora);
    }

    public static Solicitud reconstruir(UUID id, String titulo, String descripcion, String solicitante,
            String aprobador, TipoSolicitud tipo, EstadoSolicitud estado, LocalDateTime creadoEn,
            LocalDateTime actualizadoEn) {
        return new Solicitud(id, titulo, descripcion, solicitante, aprobador, tipo, estado, creadoEn, actualizadoEn);
    }

    public UUID getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public String getAprobador() {
        return aprobador;
    }

    public TipoSolicitud getTipo() {
        return tipo;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }
}
