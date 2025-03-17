package com.integracao.IntegracaoSistemaApi.excecoes;

public class EntityAlreadyPresentException extends RuntimeException {
    public EntityAlreadyPresentException(String message) {
        super(message);
    }
}
