package com.integracao.IntegracaoSistemaApi.objetosDeValor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public class Senha {
    private static final String REGRAS_SENHA = "A senha deve ter pelo menos 8 caracteres," +
            " incluindo uma letra maiúscula, " +
            "uma letra minúscula, um número e um caractere especial (@#$%^&+=!).";

    private static final String REGEX_SENHA = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";

    @JsonValue
    private final String valor;

    @JsonCreator
    public Senha(String valor) {
        verificarSeSenhaEstaEmBranco(valor);
        verificarSeSenhaEhForte(valor);
        this.valor = valor;
    }


    private void verificarSeSenhaEstaEmBranco(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("A senha não pode estar em branco");
        }
    }


    private void verificarSeSenhaEhForte(String valor) {
        if (!valor.matches(REGEX_SENHA)) {
            throw new IllegalArgumentException(REGRAS_SENHA);
        }
    }


    public static Senha converterDeString(String valor) {
        return new Senha(valor);
    }
}
