package com.kata.aprobaciones.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.HistorialCambio;
import com.kata.aprobaciones.domain.model.Solicitud;

public interface SolicitudRepository {

    Solicitud guardar(Solicitud solicitud, HistorialCambio historialInicial);

    Solicitud actualizar(Solicitud solicitud, HistorialCambio historialCambio);

    Optional<Solicitud> buscarPorId(UUID id);

    List<Solicitud> listar(String aprobador, EstadoSolicitud estado);
}
