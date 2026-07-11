package com.kata.aprobaciones.infrastructure.notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public List<Notificacion> listarPorDestinatario(String destinatario) {
        return notificacionJpaRepository.findByDestinatarioOrderByCreadoEnDesc(destinatario).stream()
                .map(NotificacionMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Notificacion> buscarPorId(UUID id) {
        return notificacionJpaRepository.findById(id).map(NotificacionMapper::toDomain);
    }

    @Override
    public void actualizar(Notificacion notificacion) {
        notificacionJpaRepository.save(NotificacionMapper.toEntity(notificacion));
    }
}
