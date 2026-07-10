package com.kata.aprobaciones.domain.port.out;

import com.kata.aprobaciones.domain.model.Notificacion;

public interface NotificacionPort {

    void notificar(Notificacion notificacion);
}
