package com.kata.aprobaciones.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kata.aprobaciones.domain.exception.AccionNoPermitidaException;
import com.kata.aprobaciones.domain.exception.SolicitudNotFoundException;
import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.HistorialCambio;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.port.in.DecidirSolicitudCommand;
import com.kata.aprobaciones.domain.port.in.RechazarSolicitudUseCase;
import com.kata.aprobaciones.domain.port.out.SolicitudRepository;

@Service
public class RechazarSolicitudService implements RechazarSolicitudUseCase {

    private static final Logger log = LoggerFactory.getLogger(RechazarSolicitudService.class);

    private final SolicitudRepository solicitudRepository;

    public RechazarSolicitudService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @Override
    public Solicitud rechazar(DecidirSolicitudCommand command)
            throws SolicitudNotFoundException, AccionNoPermitidaException {
        Solicitud solicitud = solicitudRepository.buscarPorId(command.solicitudId())
                .orElseThrow(() -> new SolicitudNotFoundException(command.solicitudId()));

        solicitud.rechazar(command.aprobador());

        HistorialCambio historial = HistorialCambio.registrar(
                solicitud.getId(), EstadoSolicitud.RECHAZADO, command.comentario(), command.aprobador());

        Solicitud actualizada = solicitudRepository.actualizar(solicitud, historial);

        log.info("Solicitud rechazada: id={}, aprobador={}", actualizada.getId(), command.aprobador());

        return actualizada;
    }
}
