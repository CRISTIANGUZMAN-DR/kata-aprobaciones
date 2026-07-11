package com.kata.aprobaciones.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kata.aprobaciones.infrastructure.persistence.entity.NotificacionEntity;

public interface NotificacionJpaRepository extends JpaRepository<NotificacionEntity, UUID> {

    List<NotificacionEntity> findByDestinatarioOrderByCreadoEnDesc(String destinatario);
}
