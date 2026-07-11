package com.kata.aprobaciones.domain.port.in;

import java.util.List;

import com.kata.aprobaciones.domain.model.HistorialCambio;
import com.kata.aprobaciones.domain.model.Solicitud;

public record SolicitudDetalle(Solicitud solicitud, List<HistorialCambio> historial) {
}
