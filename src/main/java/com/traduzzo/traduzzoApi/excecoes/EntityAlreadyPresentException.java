package com.traduzzo.traduzzoApi.excecoes;

public class EntityAlreadyPresentException extends RuntimeException {
    public EntityAlreadyPresentException(String message) {
        super(message);
    }
}
