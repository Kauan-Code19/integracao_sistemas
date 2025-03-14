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
    private static final Pattern PADRAO_NUMERO = Pattern.compile("^[0-9A-Za-z\\s]*$");
    private static final Pattern PADRAO_CEP = Pattern.compile("^\\d{0,8}$");
    private static final int TAMANHO_CEP = 8;
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

        this.logradouro = logradouro;
        this.numero = campoPreenchido(numero) ? validarNumero(numero) : null;
        this.bairro = bairro;
        this.cep = campoPreenchido(cep) ? validarCep(cep) : null;
        this.cidade = campoPreenchido(cidade) ? validarCidade(cidade) : null;
        this.estado = campoPreenchido(estado) ? validarEstado(estado) : null;
    }

    private void validarPreenchimentoCamposEndereco(
            String logradouro,
            String numero,
            String bairro,
            String cep,
            String cidade,
            String estado
    ) {
        boolean logradouroPreenchido = campoPreenchido(logradouro);
        boolean numeroPreenchido = campoPreenchido(numero);
        boolean bairroPreenchido = campoPreenchido(bairro);
        boolean cepPreenchido = campoPreenchido(cep);
        boolean cidadePreenchida = campoPreenchido(cidade);
        boolean estadoPreenchido = campoPreenchido(estado);

        boolean algumPreenchido = logradouroPreenchido || numeroPreenchido || bairroPreenchido ||
                cepPreenchido || cidadePreenchida || estadoPreenchido;

        boolean todosPreenchidos = logradouroPreenchido && numeroPreenchido && bairroPreenchido &&
                cepPreenchido && cidadePreenchida && estadoPreenchido;

        if (algumPreenchido && !todosPreenchidos) {
            throw new IllegalArgumentException(
                    "Todos os campos de endereço devem estar preenchidos ou todos vazios."
            );
        }

        if (campoBranco(logradouro) || campoBranco(numero) || campoBranco(bairro) ||
                campoBranco(cep) || campoBranco(cidade) || campoBranco(estado)) {
            throw new IllegalArgumentException("Os campos do endereço não podem estar em branco.");
        }
    }


    private boolean campoBranco(String campo) {
        return campo != null && campo.isBlank();
    }


    private boolean campoPreenchido(String campo) {
        return campo != null && !campo.isBlank();
    }


    private String validarNumero(String numero) {
        if (!PADRAO_NUMERO.matcher(numero).matches()) {
            throw new IllegalArgumentException("O número deve ser apenas numérico ou alfanumérico.");
        }

        return numero;
    }


    private String validarCep(String cep) {
        if (!PADRAO_CEP.matcher(cep).matches()) {
            throw new IllegalArgumentException(
                    "O CEP deve conter exatamente " + TAMANHO_CEP + " dígitos numéricos.");
        }

        return cep;
    }


    private String validarCidade(String cidade) {
        if (
                (cidade.length() < TAMANHO_MIN_CIDADE || cidade.length() > TAMANHO_MAX_CIDADE)
                        && !cidade.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "O nome da cidade deve ter entre " + TAMANHO_MIN_CIDADE +
                            " e " + TAMANHO_MAX_CIDADE + " caracteres."
            );
        }

        return cidade;
    }


    private String validarEstado(String estado) {
        if (!Estado.estadoValido(estado) && !estado.isBlank()) {
            throw new IllegalArgumentException("O estado deve ser uma sigla válida (ex: SP, RJ, MG).");
        }

        return estado;
    }
}
