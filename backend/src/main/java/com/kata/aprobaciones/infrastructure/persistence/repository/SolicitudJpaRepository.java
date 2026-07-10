package com.kata.aprobaciones.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kata.aprobaciones.infrastructure.persistence.entity.SolicitudEntity;

public interface SolicitudJpaRepository extends JpaRepository<SolicitudEntity, UUID> {
}
