package com.traduzzo.traduzzoApi.entidades.usuario;

import lombok.Getter;

public enum PerfilDoUsuario {
    ADMINISTRADOR("administrador"),
    COMERCIAL("comercial"),
    PROJETOS("projetos");

    @Getter
    private final String tipo;

    PerfilDoUsuario(String tipo) {
        this.tipo = tipo;
    }
}
