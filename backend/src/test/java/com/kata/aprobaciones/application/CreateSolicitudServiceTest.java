package com.kata.aprobaciones.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kata.aprobaciones.domain.exception.SolicitanteIgualAprobadorException;
import com.kata.aprobaciones.domain.model.HistorialCambio;
import com.kata.aprobaciones.domain.model.Notificacion;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.model.TipoSolicitud;
import com.kata.aprobaciones.domain.port.in.CrearSolicitudCommand;
import com.kata.aprobaciones.domain.port.out.NotificacionPort;
import com.kata.aprobaciones.domain.port.out.SolicitudRepository;

@ExtendWith(MockitoExtension.class)
class CreateSolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private NotificacionPort notificacionPort;

    private CreateSolicitudService service;

    @Test
    void should_crearSolicitudYNotificarAprobador_when_datosValidos() throws SolicitanteIgualAprobadorException {
        service = new CreateSolicitudService(solicitudRepository, notificacionPort);

        CrearSolicitudCommand command = new CrearSolicitudCommand(
                "Nueva version microservicio",
                "Aprobar despliegue de la version 2.3.0",
                "jperez",
                "mgarcia",
                TipoSolicitud.DESPLIEGUE);

        when(solicitudRepository.guardar(any(Solicitud.class), any(HistorialCambio.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Solicitud resultado = service.crear(command);

        assertThat(resultado.getSolicitante()).isEqualTo("jperez");
        assertThat(resultado.getAprobador()).isEqualTo("mgarcia");

        ArgumentCaptor<HistorialCambio> historialCaptor = ArgumentCaptor.forClass(HistorialCambio.class);
        verify(solicitudRepository).guardar(eq(resultado), historialCaptor.capture());
        assertThat(historialCaptor.getValue().getRealizadoPor()).isEqualTo("jperez");

        ArgumentCaptor<Notificacion> notificacionCaptor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificacionPort).notificar(notificacionCaptor.capture());
        assertThat(notificacionCaptor.getValue().getDestinatario()).isEqualTo("mgarcia");
        assertThat(notificacionCaptor.getValue().getSolicitudId()).isEqualTo(resultado.getId());
    }

    @Test
    void should_lanzarExcepcionYNoTocarPuertos_when_solicitanteIgualAprobador() {
        service = new CreateSolicitudService(solicitudRepository, notificacionPort);

        CrearSolicitudCommand command = new CrearSolicitudCommand(
                "Nueva version microservicio",
                "Aprobar despliegue de la version 2.3.0",
                "jperez",
                "jperez",
                TipoSolicitud.DESPLIEGUE);

        assertThatThrownBy(() -> service.crear(command))
                .isInstanceOf(SolicitanteIgualAprobadorException.class);

        verifyNoInteractions(solicitudRepository, notificacionPort);
    }
}
