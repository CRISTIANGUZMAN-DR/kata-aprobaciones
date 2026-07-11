package com.kata.aprobaciones.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.infrastructure.persistence.entity.SolicitudEntity;

public interface SolicitudJpaRepository extends JpaRepository<SolicitudEntity, UUID> {

    List<SolicitudEntity> findByAprobadorAndEstado(String aprobador, EstadoSolicitud estado);

    List<SolicitudEntity> findByAprobador(String aprobador);

    List<SolicitudEntity> findByEstado(EstadoSolicitud estado);
}
