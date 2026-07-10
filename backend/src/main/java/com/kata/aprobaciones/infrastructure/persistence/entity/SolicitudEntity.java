package com.kata.aprobaciones.infrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.TipoSolicitud;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitudes")
public class SolicitudEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String solicitante;

    @Column(nullable = false)
    private String aprobador;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSolicitud tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    protected SolicitudEntity() {
    }

    public SolicitudEntity(UUID id, String titulo, String descripcion, String solicitante, String aprobador,
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
