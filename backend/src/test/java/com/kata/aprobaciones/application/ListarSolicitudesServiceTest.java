package com.kata.aprobaciones.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kata.aprobaciones.domain.model.EstadoSolicitud;
import com.kata.aprobaciones.domain.model.Solicitud;
import com.kata.aprobaciones.domain.model.TipoSolicitud;
import com.kata.aprobaciones.domain.port.out.SolicitudRepository;

@ExtendWith(MockitoExtension.class)
class ListarSolicitudesServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Test
    void should_delegarAlRepositorio_when_seListaConFiltros() {
        ListarSolicitudesService service = new ListarSolicitudesService(solicitudRepository);

        Solicitud solicitud = Solicitud.reconstruir(
                UUID.randomUUID(), "Nueva version microservicio", "Aprobar despliegue", "jperez", "mgarcia",
                TipoSolicitud.DESPLIEGUE, EstadoSolicitud.PENDIENTE, LocalDateTime.now(), LocalDateTime.now());

        when(solicitudRepository.listar("mgarcia", EstadoSolicitud.PENDIENTE)).thenReturn(List.of(solicitud));

        List<Solicitud> resultado = service.listar("mgarcia", EstadoSolicitud.PENDIENTE);

        assertThat(resultado).containsExactly(solicitud);
    }
}
