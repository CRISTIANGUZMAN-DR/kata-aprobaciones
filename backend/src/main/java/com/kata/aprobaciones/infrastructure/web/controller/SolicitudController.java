package com.kata.aprobaciones.infrastructure.web.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kata.aprobaciones.domain.exception.SolicitanteIgualAprobadorException;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.port.in.CrearSolicitudCommand;
import com.kata.aprobaciones.domain.port.in.CreateSolicitudUseCase;
import com.kata.aprobaciones.infrastructure.web.dto.CrearSolicitudRequest;
import com.kata.aprobaciones.infrastructure.web.dto.SolicitudResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final CreateSolicitudUseCase createSolicitudUseCase;

    public SolicitudController(CreateSolicitudUseCase createSolicitudUseCase) {
        this.createSolicitudUseCase = createSolicitudUseCase;
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
}
