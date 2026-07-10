package com.kata.aprobaciones.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.kata.aprobaciones.domain.exception.AccionNoPermitidaException;
import com.kata.aprobaciones.domain.exception.SolicitanteIgualAprobadorException;
import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.model.TipoSolicitud;

class SolicitudTest {

    @Test
    void should_crearSolicitudPendiente_when_datosValidos() throws SolicitanteIgualAprobadorException {
        Solicitud solicitud = Solicitud.crear(
                "Nueva version microservicio",
                "Aprobar despliegue de la version 2.3.0",
                "jperez",
                "mgarcia",
                TipoSolicitud.DESPLIEGUE);

        assertThat(solicitud.getId()).isNotNull();
        assertThat(solicitud.getEstado()).isEqualTo(EstadoSolicitud.PENDIENTE);
        assertThat(solicitud.getCreadoEn()).isEqualTo(solicitud.getActualizadoEn());
        assertThat(solicitud.getSolicitante()).isEqualTo("jperez");
        assertThat(solicitud.getAprobador()).isEqualTo("mgarcia");
    }

    @Test
    void should_lanzarExcepcion_when_solicitanteIgualAprobador() {
        assertThatThrownBy(() -> Solicitud.crear(
                "Nueva version microservicio",
                "Aprobar despliegue de la version 2.3.0",
                "jperez",
                "jperez",
                TipoSolicitud.DESPLIEGUE))
                .isInstanceOf(SolicitanteIgualAprobadorException.class);
    }

    @Test
    void should_cambiarEstadoAAprobado_when_aprobadorDecide()
            throws SolicitanteIgualAprobadorException, AccionNoPermitidaException {
        Solicitud solicitud = Solicitud.crear(
                "Nueva version microservicio",
                "Aprobar despliegue de la version 2.3.0",
                "jperez",
                "mgarcia",
                TipoSolicitud.DESPLIEGUE);

        solicitud.aprobar("mgarcia");

        assertThat(solicitud.getEstado()).isEqualTo(EstadoSolicitud.APROBADO);
    }

    @Test
    void should_cambiarEstadoARechazado_when_aprobadorDecide()
            throws SolicitanteIgualAprobadorException, AccionNoPermitidaException {
        Solicitud solicitud = Solicitud.crear(
                "Nueva version microservicio",
                "Aprobar despliegue de la version 2.3.0",
                "jperez",
                "mgarcia",
                TipoSolicitud.DESPLIEGUE);

        solicitud.rechazar("mgarcia");

        assertThat(solicitud.getEstado()).isEqualTo(EstadoSolicitud.RECHAZADO);
    }

    @Test
    void should_lanzarExcepcion_when_usuarioDistintoAlAprobadorIntentaDecidir()
            throws SolicitanteIgualAprobadorException {
        Solicitud solicitud = Solicitud.crear(
                "Nueva version microservicio",
                "Aprobar despliegue de la version 2.3.0",
                "jperez",
                "mgarcia",
                TipoSolicitud.DESPLIEGUE);

        assertThatThrownBy(() -> solicitud.aprobar("alopez"))
                .isInstanceOf(AccionNoPermitidaException.class);
    }

    @Test
    void should_lanzarExcepcion_when_solicitudYaFueDecidida()
            throws SolicitanteIgualAprobadorException, AccionNoPermitidaException {
        Solicitud solicitud = Solicitud.crear(
                "Nueva version microservicio",
                "Aprobar despliegue de la version 2.3.0",
                "jperez",
                "mgarcia",
                TipoSolicitud.DESPLIEGUE);
        solicitud.aprobar("mgarcia");

        assertThatThrownBy(() -> solicitud.rechazar("mgarcia"))
                .isInstanceOf(AccionNoPermitidaException.class);
    }
}
