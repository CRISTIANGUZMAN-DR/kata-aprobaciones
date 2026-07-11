package com.kata.aprobaciones.infrastructure.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kata.aprobaciones.domain.model.TipoSolicitud;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    @GetMapping("/tipos")
    public ResponseEntity<List<TipoSolicitud>> tipos(@RequestHeader("X-Usuario") String usuario) {
        return ResponseEntity.ok(List.of(TipoSolicitud.values()));
    }
}
