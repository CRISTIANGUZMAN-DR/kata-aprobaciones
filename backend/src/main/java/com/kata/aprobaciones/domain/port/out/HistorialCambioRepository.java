package com.kata.aprobaciones.domain.port.out;

import java.util.List;
import java.util.UUID;

import com.kata.aprobaciones.domain.model.HistorialCambio;

public interface HistorialCambioRepository {

    List<HistorialCambio> listarPorSolicitudId(UUID solicitudId);
}
