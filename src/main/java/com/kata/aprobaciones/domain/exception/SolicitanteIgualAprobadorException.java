package com.kata.aprobaciones.domain.exception;

public class SolicitanteIgualAprobadorException extends DomainException {

    public SolicitanteIgualAprobadorException(String usuario) {
        super("El solicitante y el aprobador no pueden ser el mismo usuario: " + usuario);
    }
}
