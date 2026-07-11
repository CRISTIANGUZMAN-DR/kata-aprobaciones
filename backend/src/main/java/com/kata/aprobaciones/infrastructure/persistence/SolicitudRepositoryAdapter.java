package com.kata.aprobaciones.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.HistorialCambio;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.port.out.SolicitudRepository;
import com.kata.aprobaciones.infrastructure.persistence.entity.SolicitudEntity;
import com.kata.aprobaciones.infrastructure.persistence.mapper.HistorialCambioMapper;
import com.kata.aprobaciones.infrastructure.persistence.mapper.SolicitudMapper;
import com.kata.aprobaciones.infrastructure.persistence.repository.HistorialCambioJpaRepository;
import com.kata.aprobaciones.infrastructure.persistence.repository.SolicitudJpaRepository;

@Component
public class SolicitudRepositoryAdapter implements SolicitudRepository {

    private final SolicitudJpaRepository solicitudJpaRepository;
    private final HistorialCambioJpaRepository historialCambioJpaRepository;

    public SolicitudRepositoryAdapter(SolicitudJpaRepository solicitudJpaRepository,
            HistorialCambioJpaRepository historialCambioJpaRepository) {
        this.solicitudJpaRepository = solicitudJpaRepository;
        this.historialCambioJpaRepository = historialCambioJpaRepository;
    }

    @Override
    @Transactional
    public Solicitud guardar(Solicitud solicitud, HistorialCambio historialInicial) {
        SolicitudEntity solicitudEntity = solicitudJpaRepository.save(SolicitudMapper.toEntity(solicitud));
        historialCambioJpaRepository.save(HistorialCambioMapper.toEntity(historialInicial));
        return SolicitudMapper.toDomain(solicitudEntity);
    }

    @Override
    @Transactional
    public Solicitud actualizar(Solicitud solicitud, HistorialCambio historialCambio) {
        SolicitudEntity solicitudEntity = solicitudJpaRepository.save(SolicitudMapper.toEntity(solicitud));
        historialCambioJpaRepository.save(HistorialCambioMapper.toEntity(historialCambio));
        return SolicitudMapper.toDomain(solicitudEntity);
    }

    @Override
    public Optional<Solicitud> buscarPorId(UUID id) {
        return solicitudJpaRepository.findById(id).map(SolicitudMapper::toDomain);
    }

    @Override
    public List<Solicitud> listar(String aprobador, EstadoSolicitud estado) {
        List<SolicitudEntity> entidades;
        if (aprobador != null && estado != null) {
            entidades = solicitudJpaRepository.findByAprobadorAndEstado(aprobador, estado);
        } else if (aprobador != null) {
            entidades = solicitudJpaRepository.findByAprobador(aprobador);
        } else if (estado != null) {
            entidades = solicitudJpaRepository.findByEstado(estado);
        } else {
            entidades = solicitudJpaRepository.findAll();
        }
        return entidades.stream().map(SolicitudMapper::toDomain).toList();
    }
}
