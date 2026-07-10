package com.kata.aprobaciones.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kata.aprobaciones.infrastructure.persistence.entity.HistorialCambioEntity;

public interface HistorialCambioJpaRepository extends JpaRepository<HistorialCambioEntity, UUID> {
}
