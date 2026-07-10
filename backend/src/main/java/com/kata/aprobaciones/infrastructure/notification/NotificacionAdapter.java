package com.kata.aprobaciones.infrastructure.notification;

import org.springframework.stereotype.Component;

import com.kata.aprobaciones.domain.model.Notificacion;
import com.kata.aprobaciones.domain.port.out.NotificacionPort;
import com.kata.aprobaciones.infrastructure.persistence.mapper.NotificacionMapper;
import com.kata.aprobaciones.infrastructure.persistence.repository.NotificacionJpaRepository;

@Component
public class NotificacionAdapter implements NotificacionPort {

    private final NotificacionJpaRepository notificacionJpaRepository;

    public NotificacionAdapter(NotificacionJpaRepository notificacionJpaRepository) {
        this.notificacionJpaRepository = notificacionJpaRepository;
    }

    @Override
    public void notificar(Notificacion notificacion) {
        notificacionJpaRepository.save(NotificacionMapper.toEntity(notificacion));
    }
}
