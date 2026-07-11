package com.kata.aprobaciones.infrastructure.web.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kata.aprobaciones.domain.exception.AccionNoPermitidaException;
import com.kata.aprobaciones.domain.exception.SolicitanteIgualAprobadorException;
import com.kata.aprobaciones.domain.exception.SolicitudNotFoundException;
import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.port.in.AprobarSolicitudUseCase;
import com.kata.aprobaciones.domain.port.in.CrearSolicitudCommand;
import com.kata.aprobaciones.domain.port.in.CreateSolicitudUseCase;
import com.kata.aprobaciones.domain.port.in.DecidirSolicitudCommand;
import com.kata.aprobaciones.domain.port.in.GetSolicitudUseCase;
import com.kata.aprobaciones.domain.port.in.ListarSolicitudesUseCase;
import com.kata.aprobaciones.domain.port.in.RechazarSolicitudUseCase;
import com.kata.aprobaciones.domain.port.in.SolicitudDetalle;
import com.kata.aprobaciones.infrastructure.web.dto.CrearSolicitudRequest;
import com.kata.aprobaciones.infrastructure.web.dto.DecidirSolicitudRequest;
import com.kata.aprobaciones.infrastructure.web.dto.SolicitudDetalleResponse;
import com.kata.aprobaciones.infrastructure.web.dto.SolicitudResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final CreateSolicitudUseCase createSolicitudUseCase;
    private final AprobarSolicitudUseCase aprobarSolicitudUseCase;
    private final RechazarSolicitudUseCase rechazarSolicitudUseCase;
    private final ListarSolicitudesUseCase listarSolicitudesUseCase;
    private final GetSolicitudUseCase getSolicitudUseCase;

    public SolicitudController(CreateSolicitudUseCase createSolicitudUseCase,
            AprobarSolicitudUseCase aprobarSolicitudUseCase, RechazarSolicitudUseCase rechazarSolicitudUseCase,
            ListarSolicitudesUseCase listarSolicitudesUseCase, GetSolicitudUseCase getSolicitudUseCase) {
        this.createSolicitudUseCase = createSolicitudUseCase;
        this.aprobarSolicitudUseCase = aprobarSolicitudUseCase;
        this.rechazarSolicitudUseCase = rechazarSolicitudUseCase;
        this.listarSolicitudesUseCase = listarSolicitudesUseCase;
        this.getSolicitudUseCase = getSolicitudUseCase;
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

    @GetMapping
    public ResponseEntity<List<SolicitudResponse>> listar(
            @RequestHeader("X-Usuario") String usuario,
            @RequestParam(required = false) String aprobador,
            @RequestParam(required = false) EstadoSolicitud estado) {
        List<SolicitudResponse> response = listarSolicitudesUseCase.listar(aprobador, estado).stream()
                .map(SolicitudResponse::desde)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDetalleResponse> obtener(
            @RequestHeader("X-Usuario") String usuario,
            @PathVariable UUID id) throws SolicitudNotFoundException {
        SolicitudDetalle detalle = getSolicitudUseCase.obtener(id);
        return ResponseEntity.ok(SolicitudDetalleResponse.desde(detalle));
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
