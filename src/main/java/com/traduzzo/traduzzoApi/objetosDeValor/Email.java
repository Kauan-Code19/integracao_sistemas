package com.traduzzo.traduzzoApi.objetosDeValor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import java.util.regex.Pattern;

@Getter
public class Email {
    private static final Pattern PADRAO_EMAIL = Pattern.compile(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9]" +
            "(?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?" +
            "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    );

    @JsonValue
    private final String valor;

    @JsonCreator
    public Email(String valor) {
        verificarSeEmailEstaEmBranco(valor);
        validarFormatoOuFalhar(valor);
        this.valor = valor;
    }


    private void verificarSeEmailEstaEmBranco(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O e-mail não pode estar em branco");
        }
    }


    private void validarFormatoOuFalhar(String valor) {
        if (!PADRAO_EMAIL.matcher(valor).matches()) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }
    }

    protected static boolean formatoEmailEhValido(String valor) {
        return valor != null && PADRAO_EMAIL.matcher(valor).matches();
    }


    public static Email converterDeString(String valor) {
        return new Email(valor);
    }
}
