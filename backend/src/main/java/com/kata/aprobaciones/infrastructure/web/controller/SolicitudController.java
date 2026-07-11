package com.kata.aprobaciones.infrastructure.web.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kata.aprobaciones.domain.exception.AccionNoPermitidaException;
import com.kata.aprobaciones.domain.exception.SolicitanteIgualAprobadorException;
import com.kata.aprobaciones.domain.exception.SolicitudNotFoundException;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.port.in.AprobarSolicitudUseCase;
import com.kata.aprobaciones.domain.port.in.CrearSolicitudCommand;
import com.kata.aprobaciones.domain.port.in.CreateSolicitudUseCase;
import com.kata.aprobaciones.domain.port.in.DecidirSolicitudCommand;
import com.kata.aprobaciones.domain.port.in.RechazarSolicitudUseCase;
import com.kata.aprobaciones.infrastructure.web.dto.CrearSolicitudRequest;
import com.kata.aprobaciones.infrastructure.web.dto.DecidirSolicitudRequest;
import com.kata.aprobaciones.infrastructure.web.dto.SolicitudResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final CreateSolicitudUseCase createSolicitudUseCase;
    private final AprobarSolicitudUseCase aprobarSolicitudUseCase;
    private final RechazarSolicitudUseCase rechazarSolicitudUseCase;

    public SolicitudController(CreateSolicitudUseCase createSolicitudUseCase,
            AprobarSolicitudUseCase aprobarSolicitudUseCase, RechazarSolicitudUseCase rechazarSolicitudUseCase) {
        this.createSolicitudUseCase = createSolicitudUseCase;
        this.aprobarSolicitudUseCase = aprobarSolicitudUseCase;
        this.rechazarSolicitudUseCase = rechazarSolicitudUseCase;
    }

    @PostMapping
    public ResponseEntity<SolicitudResponse> crear(
            @RequestHeader("X-Usuario") String usuario,
            @Valid @RequestBody CrearSolicitudRequest request) throws SolicitanteIgualAprobadorException {
        CrearSolicitudCommand command = new CrearSolicitudCommand(
                request.titulo(),
                request.descripcion(),
                request.solicitante(),
                request.aprobador(),
                request.tipo());

        Solicitud solicitud = createSolicitudUseCase.crear(command);
        SolicitudResponse response = SolicitudResponse.desde(solicitud);

        return ResponseEntity.created(URI.create("/api/solicitudes/" + solicitud.getId())).body(response);
    }

    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<SolicitudResponse> aprobar(
            @PathVariable UUID id,
            @RequestHeader("X-Usuario") String usuario,
            @Valid @RequestBody DecidirSolicitudRequest request)
            throws SolicitudNotFoundException, AccionNoPermitidaException {
        DecidirSolicitudCommand command = new DecidirSolicitudCommand(id, request.aprobador(), request.comentario());
        Solicitud solicitud = aprobarSolicitudUseCase.aprobar(command);
        return ResponseEntity.ok(SolicitudResponse.desde(solicitud));
    }

    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<SolicitudResponse> rechazar(
            @PathVariable UUID id,
            @RequestHeader("X-Usuario") String usuario,
            @Valid @RequestBody DecidirSolicitudRequest request)
            throws SolicitudNotFoundException, AccionNoPermitidaException {
        DecidirSolicitudCommand command = new DecidirSolicitudCommand(id, request.aprobador(), request.comentario());
        Solicitud solicitud = rechazarSolicitudUseCase.rechazar(command);
        return ResponseEntity.ok(SolicitudResponse.desde(solicitud));
    }
}
