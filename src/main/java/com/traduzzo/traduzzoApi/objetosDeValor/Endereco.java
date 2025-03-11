package com.traduzzo.traduzzoApi.objetosDeValor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.traduzzo.traduzzoApi.enumeracoes.Estado;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.regex.Pattern;

@Getter
@Embeddable
@NoArgsConstructor(force = true)
public class Endereco {
    private static final Pattern PADRAO_LOGRADOURO = Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$");
    private static final Pattern PADRAO_NUMERO = Pattern.compile("^[0-9A-Za-z]+$");
    private static final Pattern PADRAO_CEP = Pattern.compile("^\\d{8}$");
    private static final int TAMANHO_MIN_CIDADE = 6;
    private static final int TAMANHO_MAX_CIDADE = 30;

    private final String logradouro;
    private final String numero;
    private final String bairro;
    private final String cep;
    private final String cidade;
    private final String estado;

    @JsonCreator
    public Endereco(
            String logradouro,
            String numero,
            String bairro,
            String cep,
            String cidade,
            String estado
    ) {
        validarPreenchimentoCamposEndereco(logradouro, numero, bairro, cep, cidade, estado);

        validarLogradouro(logradouro);
        this.logradouro = logradouro;

        validarNumero(numero);
        this.numero = numero;

        this.bairro = bairro;

        validarCep(cep);
        this.cep = cep;

        validarCidade(cidade);
        this.cidade = cidade;

        validarEstado(estado);
        this.estado = estado;
    }

    private void validarPreenchimentoCamposEndereco(
            String logradouro,
            String numero,
            String bairro,
            String cep,
            String cidade,
            String estado
    ) {
        boolean algumPreenchido = campoPreenchido(logradouro) || campoPreenchido(numero) ||
                campoPreenchido(bairro) || campoPreenchido(cep) ||
                campoPreenchido(cidade) || campoPreenchido(estado);

        boolean todosPreenchidos = campoPreenchido(logradouro) && campoPreenchido(numero) &&
                campoPreenchido(bairro) && campoPreenchido(cep) &&
                campoPreenchido(cidade) && campoPreenchido(estado);

        if (algumPreenchido && !todosPreenchidos) {
            throw new IllegalArgumentException("Todos os campos de endereço devem" +
                    " estar preenchidos ou todos vazios.");
        }
    }


    private boolean campoPreenchido(String campo) {
        return campo != null && !campo.isBlank();
    }


    private void validarLogradouro(String logradouro) {
        if (!PADRAO_LOGRADOURO.matcher(logradouro).matches()) {
            throw new IllegalArgumentException("O logradouro deve conter apenas letras e espaços.");
        }
    }


    private void validarNumero(String numero) {
        if (!PADRAO_NUMERO.matcher(numero).matches()) {
            throw new IllegalArgumentException("O número deve ser apenas numérico ou alfanumérico.");
        }
    }


    private void validarCep(String cep) {
        if (!PADRAO_CEP.matcher(cep).matches()) {
            throw new IllegalArgumentException("O CEP deve conter exatamente 8 dígitos numéricos.");
        }
    }


    private void validarCidade(String cidade) {
        if (cidade.length() < TAMANHO_MIN_CIDADE || cidade.length() > TAMANHO_MAX_CIDADE) {
            throw new IllegalArgumentException("O nome da cidade deve ter entre 6 e 30 caracteres.");
        }
    }


    private void validarEstado(String estado) {
        if (!Estado.estadoValido(estado)) {
            throw new IllegalArgumentException("O estado deve ser uma sigla válida (ex: SP, RJ, MG).");
        }
    }
}
