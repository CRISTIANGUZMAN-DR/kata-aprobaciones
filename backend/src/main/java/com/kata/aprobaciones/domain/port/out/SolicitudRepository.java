package com.kata.aprobaciones.domain.port.out;

import com.kata.aprobaciones.domain.model.HistorialCambio;
import com.kata.aprobaciones.domain.model.Solicitud;

public interface SolicitudRepository {

    Solicitud guardar(Solicitud solicitud, HistorialCambio historialInicial);
}
