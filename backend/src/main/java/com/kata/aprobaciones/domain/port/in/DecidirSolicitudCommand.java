package com.kata.aprobaciones.domain.port.in;

import java.util.UUID;

public record DecidirSolicitudCommand(UUID solicitudId, String aprobador, String comentario) {
}
