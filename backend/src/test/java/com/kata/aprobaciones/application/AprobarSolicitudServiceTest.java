package com.kata.aprobaciones.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kata.aprobaciones.domain.exception.AccionNoPermitidaException;
import com.kata.aprobaciones.domain.exception.SolicitudNotFoundException;
import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.HistorialCambio;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.model.TipoSolicitud;
import com.kata.aprobaciones.domain.port.in.DecidirSolicitudCommand;
import com.kata.aprobaciones.domain.port.out.SolicitudRepository;

@ExtendWith(MockitoExtension.class)
class AprobarSolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Test
    void should_aprobarSolicitud_when_aprobadorCorrecto() throws Exception {
        AprobarSolicitudService service = new AprobarSolicitudService(solicitudRepository);

        UUID solicitudId = UUID.randomUUID();
        Solicitud solicitud = Solicitud.reconstruir(
                solicitudId, "Nueva version microservicio", "Aprobar despliegue", "jperez", "mgarcia",
                TipoSolicitud.DESPLIEGUE, EstadoSolicitud.PENDIENTE,
                java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        when(solicitudRepository.buscarPorId(solicitudId)).thenReturn(Optional.of(solicitud));
        when(solicitudRepository.actualizar(any(Solicitud.class), any(HistorialCambio.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DecidirSolicitudCommand command = new DecidirSolicitudCommand(solicitudId, "mgarcia", "Todo en orden");
        Solicitud resultado = service.aprobar(command);

        assertThat(resultado.getEstado()).isEqualTo(EstadoSolicitud.APROBADO);
        verify(solicitudRepository).actualizar(any(Solicitud.class), any(HistorialCambio.class));
    }

    @Test
    void should_lanzarExcepcion_when_solicitudNoExiste() {
        AprobarSolicitudService service = new AprobarSolicitudService(solicitudRepository);

        UUID solicitudId = UUID.randomUUID();
        when(solicitudRepository.buscarPorId(solicitudId)).thenReturn(Optional.empty());

        DecidirSolicitudCommand command = new DecidirSolicitudCommand(solicitudId, "mgarcia", null);

        assertThatThrownBy(() -> service.aprobar(command))
                .isInstanceOf(SolicitudNotFoundException.class);
    }

    @Test
    void should_lanzarExcepcion_when_usuarioNoEsElAprobador() {
        AprobarSolicitudService service = new AprobarSolicitudService(solicitudRepository);

        UUID solicitudId = UUID.randomUUID();
        Solicitud solicitud = Solicitud.reconstruir(
                solicitudId, "Nueva version microservicio", "Aprobar despliegue", "jperez", "mgarcia",
                TipoSolicitud.DESPLIEGUE, EstadoSolicitud.PENDIENTE,
                java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        when(solicitudRepository.buscarPorId(solicitudId)).thenReturn(Optional.of(solicitud));

        DecidirSolicitudCommand command = new DecidirSolicitudCommand(solicitudId, "alopez", null);

        assertThatThrownBy(() -> service.aprobar(command))
                .isInstanceOf(AccionNoPermitidaException.class);
    }
}
