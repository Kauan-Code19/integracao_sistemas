package com.traduzzo.traduzzoApi.objetosDeValor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import java.util.regex.Pattern;

@Getter
public class Cpf {
    private static final Pattern FORMATO_CPF = Pattern.compile("\\d{11}");
    private static final int PESO_PENULTIMO_DIGITO = 10;
    private static final int PESO_ULTIMO_DIGITO = 11;
    private static final int MODULO_CPF = 11;
    private final int penultimoDigito;

    @JsonValue
    private final String valor;

    @JsonCreator
    public Cpf(String valor) {
        verificarSeCpfEstaEmBranco(valor);
        validarFormatoOuFalhar(valor);
        this.penultimoDigito = obterPenultimoDigito(valor);
        verificarPenultimoDigitoVerificador(valor);
        verificarUltimoDigitoVerificador(valor);
        this.valor = valor;
    }


    private void verificarSeCpfEstaEmBranco(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O cpf não pode estar em branco");
        }
    }


    private void validarFormatoOuFalhar(String valor) {
        if (!FORMATO_CPF.matcher(valor).matches()) {
            throw new IllegalArgumentException("Formato do cpf inválido");
        }

        verificarSequenciaisInvalidosPorBloco(valor);
    }


    protected static boolean formatoCpfEhValido(String valor) {
        if (!FORMATO_CPF.matcher(valor).matches()) {
            return false;
        }

        try {
            verificarSequenciaisInvalidosPorBloco(valor);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }


    private static void verificarSequenciaisInvalidosPorBloco(String valor) {
        for (int i = 0; i < 3; i++) {
            String bloco = valor.substring(i * 3, (i + 1) * 3);
            if (repetido(bloco)) {
                throw new IllegalArgumentException("O cpf contém blocos com dígitos repetidos: " + bloco);
            }
        }
    }


    private static boolean repetido(String bloco) {
        return bloco.charAt(0) == bloco.charAt(1) && bloco.charAt(1) == bloco.charAt(2);
    }


    private void verificarPenultimoDigitoVerificador(String valor) {
        int somaMultiplicada = calcularSomaMultiplicadaPorDigitos(valor);
        int digitoVerificadorCalculado = calcularDigitoVerificador(somaMultiplicada);

        if (digitoVerificadorIncorreto(digitoVerificadorCalculado, this.penultimoDigito)) {
            throw new IllegalArgumentException(
                    "O penúltimo dígito verificador está incorreto: " + this.penultimoDigito
            );
        }
    }


    private int calcularSomaMultiplicadaPorDigitos(String valor) {
        int soma = 0;
        int peso = PESO_PENULTIMO_DIGITO;

        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(valor.charAt(i)) * peso;
            peso--;
        }

        return soma;
    }


    private int obterPenultimoDigito(String valor) {
        return Character.getNumericValue(valor.charAt(valor.length() - 2));
    }


    private void verificarUltimoDigitoVerificador(String valor) {
        int somaMultiplicada = calcularSomaMultiplicadaPorDigitos(valor, this.penultimoDigito);
        int digitoVerificadorCalculado = calcularDigitoVerificador(somaMultiplicada);

        int ultimoDigito = obterUltimoDigito(valor);

        if (digitoVerificadorIncorreto(digitoVerificadorCalculado, ultimoDigito)) {
            throw new IllegalArgumentException(
                    "O último dígito verificador está incorreto: " + ultimoDigito
            );
        }
    }


    private int calcularSomaMultiplicadaPorDigitos(String valor, int penultimoDigito) {
        int soma = 0;
        int peso = PESO_ULTIMO_DIGITO;

        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(valor.charAt(i)) * peso;
            peso--;
        }

        soma += penultimoDigito * peso;

        return soma;
    }


    private int calcularDigitoVerificador(int soma) {
        return soma % MODULO_CPF;
    }


    private int obterUltimoDigito(String valor) {
        return Character.getNumericValue(valor.charAt(valor.length() - 1));
    }


    private boolean digitoVerificadorIncorreto(int digitoVerificadorCalculado, int digitoVerificadorInformado) {
        int digitoEsperado = calcularDigitoEsperado(digitoVerificadorCalculado);
        return digitoEsperado != digitoVerificadorInformado;
    }


    private int calcularDigitoEsperado(int digitoVerificadorCalculado) {
        if (digitoVerificadorCalculado < 2) {
            return 0;
        }

        return MODULO_CPF - digitoVerificadorCalculado;
    }
}
