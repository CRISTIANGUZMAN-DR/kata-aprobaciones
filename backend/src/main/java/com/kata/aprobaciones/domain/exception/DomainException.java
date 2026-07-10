package com.kata.aprobaciones.domain.exception;

public abstract class DomainException extends Exception {

    protected DomainException(String message) {
        super(message);
    }
}
