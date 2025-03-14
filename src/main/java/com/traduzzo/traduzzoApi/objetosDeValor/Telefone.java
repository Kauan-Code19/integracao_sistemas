package com.traduzzo.traduzzoApi.objetosDeValor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Telefone {
    private static final Pattern PADRAO_TELEFONE_CELULAR = Pattern.compile(
            "^\\+55\\d{2}9\\d{8}$"
    );

    private static final Pattern PADRAO_TELEFONE_FIXO = Pattern.compile(
            "^\\+55\\d{2}\\d{8}$"
    );

    private static final Pattern PADRAO_CODIGO_DE_AREA = Pattern.compile("\\+55\\s?\\(?(\\d{2})\\)?");

    @JsonValue
    private final String valor;

    @JsonCreator
    public Telefone(String valor) {
        verificarSeTelefoneEstaEmBranco(valor);
        verificarSeTelefoneTemFormatoValido(valor);
        verificarCodigoDePais(valor);
        verificarCodigoDeArea(valor);
        this.valor = valor;
    }


    private void verificarSeTelefoneEstaEmBranco(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O telefone não pode estar em branco");
        }
    }


    private void verificarSeTelefoneTemFormatoValido(String valor) {
        if (!PADRAO_TELEFONE_CELULAR.matcher(valor).matches() && !PADRAO_TELEFONE_FIXO.matcher(valor).matches()) {
            throw new IllegalArgumentException(
                    "Formato inválido! Use '+55DDD9XXXXXXXX' para celular ou '+55DDDXXXXXXXX' para fixo.");
        }
    }


    private void verificarCodigoDePais(String valor) {
        if (!valor.startsWith("+55") && !valor.startsWith("00 55")) {
            throw new IllegalArgumentException("O código do país deve ser +55 ou 00 55");
        }
    }


    private void verificarCodigoDeArea(String valor) {
        Matcher matcher = PADRAO_CODIGO_DE_AREA.matcher(valor);
        if (!matcher.find()) {
            throw new IllegalArgumentException("DDD inválido");
        }

        String ddd = matcher.group(1);
        if (ddd.length() != 2) {
            throw new IllegalArgumentException("O DDD deve ter 2 dígitos");
        }
    }
}
