package com.kata.aprobaciones.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.port.in.ListarSolicitudesUseCase;
import com.kata.aprobaciones.domain.port.out.SolicitudRepository;

@Service
public class ListarSolicitudesService implements ListarSolicitudesUseCase {

    private final SolicitudRepository solicitudRepository;

    public ListarSolicitudesService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @Override
    public List<Solicitud> listar(String aprobador, EstadoSolicitud estado) {
        return solicitudRepository.listar(aprobador, estado);
    }
}
