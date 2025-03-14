package com.traduzzo.traduzzoApi.objetosDeValor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import java.util.List;
import java.util.regex.Pattern;

@Getter
public class NomeCompleto {
    private static final Pattern PADRAO_NOME_COMPLETO = Pattern.compile(
            "^(?!.*\\s{2,})(?!.*-{2,})[A-Za-zÀ-ÖØ-öø-ÿ]+(?:[-\\s][A-Za-zÀ-ÖØ-öø-ÿ]+)*$"
    );
    private static final List<String> ELEMENTOS_DE_LIGACAO = List.of("de", "da", "do", "e");
    private static final int MAX_NOMES_PROPRIOS = 2;
    private static final int MIN_PALAVRAS = 2;

    @JsonValue
    private final String valor;

    @JsonCreator
    public NomeCompleto(String valor) {
        validarNome(valor);
        this.valor = valor;
    }

    private void validarNome(String valor) {
        verificarSeNomeEstaEmBranco(valor);
        verificarSeNomePossuiApenasLetras(valor);
        String[] palavras = quebrarNomeEmPalavras(valor);
        verificarQuantidadeMinimaDePalavras(palavras);

        String[] nome = extrairNome(palavras);
        String[] sobrenome = extrairSobrenome(palavras);

        verificarLimiteDeNomesProprios(nome);
        verificarSobrenome(sobrenome);
    }


    private void verificarSeNomeEstaEmBranco(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O nome completo não pode estar em branco.");
        }
    }


    private void verificarSeNomePossuiApenasLetras(String valor) {
        if (!PADRAO_NOME_COMPLETO.matcher(valor).matches()) {
            throw new IllegalArgumentException(
                    "O nome completo deve conter apenas letras, espaços simples e hífens simples."
            );
        }
    }


    private String[] quebrarNomeEmPalavras(String valor) {
        return valor.trim().split("\\s+");
    }


    private void verificarQuantidadeMinimaDePalavras(String[] palavras) {
        if (palavras.length < MIN_PALAVRAS) {
            throw new IllegalArgumentException(
                    "O nome completo deve conter pelo menos um nome e um sobrenome."
            );
        }
    }


    private String[] extrairNome(String[] palavras) {
        int limiteNome = Math.min(2, palavras.length);
        String[] nome = new String[limiteNome];
        System.arraycopy(palavras, 0, nome, 0, limiteNome);
        return nome;
    }


    private String[] extrairSobrenome(String[] palavras) {
        int limiteNome = Math.min(2, palavras.length);
        int sobrenomeTamanho = palavras.length - limiteNome;

        if (sobrenomeTamanho <= 0) {
            return new String[0];
        }

        String[] sobrenome = new String[sobrenomeTamanho];
        System.arraycopy(palavras, limiteNome, sobrenome, 0, sobrenomeTamanho);
        return sobrenome;
    }


    private void verificarLimiteDeNomesProprios(String[] nome) {
        int nomesProprios = 0;

        for (String palavra : nome) {
            if (!ELEMENTOS_DE_LIGACAO.contains(palavra.toLowerCase())) {
                nomesProprios++;
            }
        }

        if (nomesProprios > MAX_NOMES_PROPRIOS) {
            throw new IllegalArgumentException(
                    "O nome pode conter no máximo " + MAX_NOMES_PROPRIOS + " nomes próprios."
            );
        }
    }


    private void verificarSobrenome(String[] sobrenome) {
        if (sobrenome.length == 0) {
            throw new IllegalArgumentException("O nome completo deve conter pelo menos um sobrenome.");
        }
    }
}