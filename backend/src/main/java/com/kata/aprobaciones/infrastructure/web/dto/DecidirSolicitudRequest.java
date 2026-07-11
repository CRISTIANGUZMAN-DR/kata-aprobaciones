package com.kata.aprobaciones.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record DecidirSolicitudRequest(@NotBlank String aprobador, String comentario) {
}
