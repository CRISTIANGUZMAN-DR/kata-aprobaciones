package com.kata.aprobaciones.domain.port.in;

import java.util.List;

import com.kata.aprobaciones.domain.model.Notificacion;

public interface ListarNotificacionesUseCase {

    List<Notificacion> listar(String usuario);
}
