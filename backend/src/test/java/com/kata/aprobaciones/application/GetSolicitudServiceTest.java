package com.kata.aprobaciones.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
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
import com.kata.aprobaciones.domain.port.in.SolicitudDetalle;
import com.kata.aprobaciones.domain.port.out.HistorialCambioRepository;
import com.kata.aprobaciones.domain.port.out.SolicitudRepository;

@ExtendWith(MockitoExtension.class)
class GetSolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private HistorialCambioRepository historialCambioRepository;

    @Test
    void should_retornarSolicitudConHistorial_when_existe() throws SolicitudNotFoundException {
        GetSolicitudService service = new GetSolicitudService(solicitudRepository, historialCambioRepository);

        UUID solicitudId = UUID.randomUUID();
        Solicitud solicitud = Solicitud.reconstruir(
                solicitudId, "Nueva version microservicio", "Aprobar despliegue", "jperez", "mgarcia",
                TipoSolicitud.DESPLIEGUE, EstadoSolicitud.PENDIENTE, LocalDateTime.now(), LocalDateTime.now());
        HistorialCambio historial = HistorialCambio.registrar(solicitudId, EstadoSolicitud.PENDIENTE, null, "jperez");

        when(solicitudRepository.buscarPorId(solicitudId)).thenReturn(Optional.of(solicitud));
        when(historialCambioRepository.listarPorSolicitudId(solicitudId)).thenReturn(List.of(historial));

        SolicitudDetalle detalle = service.obtener(solicitudId);

        assertThat(detalle.solicitud()).isEqualTo(solicitud);
        assertThat(detalle.historial()).hasSize(1);
    }

    @Test
    void should_lanzarExcepcion_when_solicitudNoExiste() {
        GetSolicitudService service = new GetSolicitudService(solicitudRepository, historialCambioRepository);

        UUID solicitudId = UUID.randomUUID();
        when(solicitudRepository.buscarPorId(solicitudId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtener(solicitudId))
                .isInstanceOf(SolicitudNotFoundException.class);
    }
}
