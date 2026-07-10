package com.kata.aprobaciones.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kata.aprobaciones.domain.exception.SolicitanteIgualAprobadorException;
import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.HistorialCambio;
import com.kata.aprobaciones.domain.model.Notificacion;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.port.in.CrearSolicitudCommand;
import com.kata.aprobaciones.domain.port.in.CreateSolicitudUseCase;
import com.kata.aprobaciones.domain.port.out.NotificacionPort;
import com.kata.aprobaciones.domain.port.out.SolicitudRepository;

@Service
public class CreateSolicitudService implements CreateSolicitudUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateSolicitudService.class);

    private final SolicitudRepository solicitudRepository;
    private final NotificacionPort notificacionPort;

    public CreateSolicitudService(SolicitudRepository solicitudRepository, NotificacionPort notificacionPort) {
        this.solicitudRepository = solicitudRepository;
        this.notificacionPort = notificacionPort;
    }

    @Override
    public Solicitud crear(CrearSolicitudCommand command) throws SolicitanteIgualAprobadorException {
        Solicitud solicitud = Solicitud.crear(
                command.titulo(),
                command.descripcion(),
                command.solicitante(),
                command.aprobador(),
                command.tipo());

        HistorialCambio historialInicial = HistorialCambio.registrar(
                solicitud.getId(),
                EstadoSolicitud.PENDIENTE,
                null,
                solicitud.getSolicitante());

        Solicitud solicitudGuardada = solicitudRepository.guardar(solicitud, historialInicial);

        Notificacion notificacion = Notificacion.crear(
                solicitudGuardada.getAprobador(),
                solicitudGuardada.getId(),
                "Tienes una nueva solicitud pendiente de aprobación: " + solicitudGuardada.getTitulo());
        notificacionPort.notificar(notificacion);

        log.info("Solicitud creada: id={}, solicitante={}, aprobador={}, tipo={}",
                solicitudGuardada.getId(), solicitudGuardada.getSolicitante(),
                solicitudGuardada.getAprobador(), solicitudGuardada.getTipo());

        return solicitudGuardada;
    }
}
