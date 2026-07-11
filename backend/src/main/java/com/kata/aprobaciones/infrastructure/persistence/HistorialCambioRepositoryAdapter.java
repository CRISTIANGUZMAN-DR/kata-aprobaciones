package com.kata.aprobaciones.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.kata.aprobaciones.domain.model.HistorialCambio;
import com.kata.aprobaciones.domain.port.out.HistorialCambioRepository;
import com.kata.aprobaciones.infrastructure.persistence.mapper.HistorialCambioMapper;
import com.kata.aprobaciones.infrastructure.persistence.repository.HistorialCambioJpaRepository;

@Component
public class HistorialCambioRepositoryAdapter implements HistorialCambioRepository {

    private final HistorialCambioJpaRepository historialCambioJpaRepository;

    public HistorialCambioRepositoryAdapter(HistorialCambioJpaRepository historialCambioJpaRepository) {
        this.historialCambioJpaRepository = historialCambioJpaRepository;
    }

    @Override
    public List<HistorialCambio> listarPorSolicitudId(UUID solicitudId) {
        return historialCambioJpaRepository.findBySolicitudIdOrderByFechaAsc(solicitudId).stream()
                .map(HistorialCambioMapper::toDomain)
                .toList();
    }
}
