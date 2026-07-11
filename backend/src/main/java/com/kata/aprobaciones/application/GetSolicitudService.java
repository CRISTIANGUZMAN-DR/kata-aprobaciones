package com.kata.aprobaciones.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kata.aprobaciones.domain.exception.SolicitudNotFoundException;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.port.in.GetSolicitudUseCase;
import com.kata.aprobaciones.domain.port.in.SolicitudDetalle;
import com.kata.aprobaciones.domain.port.out.HistorialCambioRepository;
import com.kata.aprobaciones.domain.port.out.SolicitudRepository;

@Service
public class GetSolicitudService implements GetSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final HistorialCambioRepository historialCambioRepository;

    public GetSolicitudService(SolicitudRepository solicitudRepository,
            HistorialCambioRepository historialCambioRepository) {
        this.solicitudRepository = solicitudRepository;
        this.historialCambioRepository = historialCambioRepository;
    }

    @Override
    public SolicitudDetalle obtener(UUID id) throws SolicitudNotFoundException {
        Solicitud solicitud = solicitudRepository.buscarPorId(id)
                .orElseThrow(() -> new SolicitudNotFoundException(id));
        return new SolicitudDetalle(solicitud, historialCambioRepository.listarPorSolicitudId(id));
    }
}
