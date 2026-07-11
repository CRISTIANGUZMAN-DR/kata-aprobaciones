package com.kata.aprobaciones.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kata.aprobaciones.domain.model.Notificacion;
import com.kata.aprobaciones.domain.port.in.ListarNotificacionesUseCase;
import com.kata.aprobaciones.domain.port.out.NotificacionPort;

@Service
public class ListarNotificacionesService implements ListarNotificacionesUseCase {

    private final NotificacionPort notificacionPort;

    public ListarNotificacionesService(NotificacionPort notificacionPort) {
        this.notificacionPort = notificacionPort;
    }

    @Override
    public List<Notificacion> listar(String usuario) {
        return notificacionPort.listarPorDestinatario(usuario);
    }
}
