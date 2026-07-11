package com.kata.aprobaciones.infrastructure.web.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kata.aprobaciones.domain.exception.NotificacionNotFoundException;
import com.kata.aprobaciones.domain.model.Notificacion;
import com.kata.aprobaciones.domain.port.in.ListarNotificacionesUseCase;
import com.kata.aprobaciones.domain.port.in.MarcarNotificacionLeidaUseCase;
import com.kata.aprobaciones.infrastructure.web.dto.NotificacionResponse;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final ListarNotificacionesUseCase listarNotificacionesUseCase;
    private final MarcarNotificacionLeidaUseCase marcarNotificacionLeidaUseCase;

    public NotificacionController(ListarNotificacionesUseCase listarNotificacionesUseCase,
            MarcarNotificacionLeidaUseCase marcarNotificacionLeidaUseCase) {
        this.listarNotificacionesUseCase = listarNotificacionesUseCase;
        this.marcarNotificacionLeidaUseCase = marcarNotificacionLeidaUseCase;
    }

    @GetMapping("/{usuario}")
    public ResponseEntity<List<NotificacionResponse>> listar(
            @RequestHeader("X-Usuario") String usuarioHeader,
            @PathVariable String usuario) {
        List<NotificacionResponse> response = listarNotificacionesUseCase.listar(usuario).stream()
                .map(NotificacionResponse::desde)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/leer")
    public ResponseEntity<NotificacionResponse> marcarLeida(
            @RequestHeader("X-Usuario") String usuarioHeader,
            @PathVariable UUID id) throws NotificacionNotFoundException {
        Notificacion notificacion = marcarNotificacionLeidaUseCase.marcarLeida(id);
        return ResponseEntity.ok(NotificacionResponse.desde(notificacion));
    }
}
