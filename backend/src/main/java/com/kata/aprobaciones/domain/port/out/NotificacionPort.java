package com.kata.aprobaciones.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kata.aprobaciones.domain.model.Notificacion;

public interface NotificacionPort {

    void notificar(Notificacion notificacion);

    List<Notificacion> listarPorDestinatario(String destinatario);

    Optional<Notificacion> buscarPorId(UUID id);

    void actualizar(Notificacion notificacion);
}
