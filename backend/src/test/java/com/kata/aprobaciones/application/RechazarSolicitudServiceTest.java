package com.kata.aprobaciones.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kata.aprobaciones.domain.exception.SolicitudNotFoundException;
import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.HistorialCambio;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.model.TipoSolicitud;
import com.kata.aprobaciones.domain.port.in.DecidirSolicitudCommand;
import com.kata.aprobaciones.domain.port.out.SolicitudRepository;

@ExtendWith(MockitoExtension.class)
class RechazarSolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Test
    void should_rechazarSolicitud_when_aprobadorCorrecto() throws Exception {
        RechazarSolicitudService service = new RechazarSolicitudService(solicitudRepository);

        UUID solicitudId = UUID.randomUUID();
        Solicitud solicitud = Solicitud.reconstruir(
                solicitudId, "Nueva version microservicio", "Aprobar despliegue", "jperez", "mgarcia",
                TipoSolicitud.DESPLIEGUE, EstadoSolicitud.PENDIENTE,
                java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        when(solicitudRepository.buscarPorId(solicitudId)).thenReturn(Optional.of(solicitud));
        when(solicitudRepository.actualizar(any(Solicitud.class), any(HistorialCambio.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DecidirSolicitudCommand command = new DecidirSolicitudCommand(solicitudId, "mgarcia", "No cumple politicas");
        Solicitud resultado = service.rechazar(command);

        assertThat(resultado.getEstado()).isEqualTo(EstadoSolicitud.RECHAZADO);
    }

    @Test
    void should_lanzarExcepcion_when_solicitudNoExiste() {
        RechazarSolicitudService service = new RechazarSolicitudService(solicitudRepository);

        UUID solicitudId = UUID.randomUUID();
        when(solicitudRepository.buscarPorId(solicitudId)).thenReturn(Optional.empty());

        DecidirSolicitudCommand command = new DecidirSolicitudCommand(solicitudId, "mgarcia", null);

        assertThatThrownBy(() -> service.rechazar(command))
                .isInstanceOf(SolicitudNotFoundException.class);
    }
}
