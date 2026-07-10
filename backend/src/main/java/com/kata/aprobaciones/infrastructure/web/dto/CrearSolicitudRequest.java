package com.kata.aprobaciones.infrastructure.web.dto;

import com.kata.aprobaciones.domain.model.TipoSolicitud;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearSolicitudRequest(
        @NotBlank String titulo,
        @NotBlank String descripcion,
        @NotBlank String solicitante,
        @NotBlank String aprobador,
        @NotNull TipoSolicitud tipo) {
}
