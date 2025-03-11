package com.traduzzo.traduzzoApi.objetosDeValor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import java.util.List;

@Getter
public class NomeCompleto {
    private static final List<String> ELEMENTOS_DE_LIGACAO = List.of("de", "da", "do", "e");

    @JsonValue
    private final String valor;

    @JsonCreator
    public NomeCompleto(String valor) {
        verificarSeNomeCompletoEstaEmBranco(valor);
        verificarSeNomeCompletoTemFormatoValido(valor);
        this.valor = valor;
    }


    private void verificarSeNomeCompletoEstaEmBranco(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O nome completo não pode estar em branco");
        }
    }


    private void verificarSeNomeCompletoTemFormatoValido(String valor) {
        String[] palavras = valor.trim().split("\\s+");

        int contadorVocabulosSimples = 0;
        int contadorVocabulosCompostos = 0;

        for (String palavra : palavras) {
            if (!ELEMENTOS_DE_LIGACAO.contains(palavra.toLowerCase())) {
                if (palavra.contains("-")) {
                    contadorVocabulosCompostos++;
                } else {
                    contadorVocabulosSimples++;
                }
            }
        }

        verificarLimiteDeVocabulos(contadorVocabulosSimples, contadorVocabulosCompostos);
//        verificarLimiteDeNomesProprios(contadorVocabulosSimples);
        verificarLimiteDeApelidos(contadorVocabulosCompostos);
    }


    private void verificarLimiteDeVocabulos(int contadorSimples, int contadorCompostos) {
        if (contadorSimples + contadorCompostos > 6) {
            throw new IllegalArgumentException(
                    "O nome completo pode conter no máximo seis palavras," +
                            " incluindo nomes e apelidos. Tente reduzir."
            );
        }
    }


//    private void verificarLimiteDeNomesProprios(int contadorSimples) {
//        if (contadorSimples > 2) {
//            throw new IllegalArgumentException("O nome completo pode conter no máximo dois nomes próprios.");
//        }
//    }


    private void verificarLimiteDeApelidos(int contadorCompostos) {
        if (contadorCompostos > 4) {
            throw new IllegalArgumentException("O nome completo pode conter no máximo quatro apelidos.");
        }
    }
}
