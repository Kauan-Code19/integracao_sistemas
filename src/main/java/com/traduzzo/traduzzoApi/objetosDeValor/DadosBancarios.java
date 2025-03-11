package com.traduzzo.traduzzoApi.objetosDeValor;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.regex.Pattern;

@Getter
@Embeddable
@NoArgsConstructor(force = true)
public class DadosBancarios {
    private static final Pattern FORMATO_AGENCIA = Pattern.compile("^\\d{1,5}(-\\d{0,2})?$");
    private static final Pattern FORMATO_CONTA = Pattern.compile("^\\d{1,12}(-\\d{0,2})?$");
    private static final Pattern PADRAO_CHAVE_ALEATORIA = Pattern.compile(
            "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"
    );

    private final String banco;
    private final String conta;
    private final String agencia;
    private final String chavePix;

    @JsonCreator
    public DadosBancarios(String banco, String conta, String agencia, String chavePix) {
        validarPreenchimentoCamposBancarios(banco, conta, agencia);

        this.banco = banco;

        verificarSeContaTemFormatoValido(conta);
        this.conta = conta;

        verificarSeAgenciaTemFormatoValido(agencia);
        this.agencia = agencia;

        this.chavePix = validarTipoDeChave(chavePix);
    }


    private void validarPreenchimentoCamposBancarios(String banco, String conta, String agencia) {
        boolean algumPreenchido = campoPreenchido(banco) || campoPreenchido(conta) || campoPreenchido(agencia);
        boolean todosPreenchidos = campoPreenchido(banco) && campoPreenchido(conta) && campoPreenchido(agencia);

        if (algumPreenchido && !todosPreenchidos) {
            throw new IllegalArgumentException("Banco, conta e agência devem estar todos" +
                    " preenchidos ou todos vazios.");
        }
    }


    private boolean campoPreenchido(String campo) {
        return campo != null && !campo.isBlank();
    }


    private void verificarSeContaTemFormatoValido(String conta) {
        if (!FORMATO_CONTA.matcher(conta).matches()) {
            throw new IllegalArgumentException("A conta deve ter entre 1 e 12 números," +
                    " podendo conter um dígito de 0 a 2 caracteres separado por hífen");
        }
    }


    private void verificarSeAgenciaTemFormatoValido(String agencia) {
        if (!FORMATO_AGENCIA.matcher(agencia).matches()) {
            throw new IllegalArgumentException("A agência deve ter entre 1 e 5 números," +
                    " podendo conter um dígito de 0 a 2 caracteres separado por hífen");
        }
    }


    private String validarTipoDeChave(String chavePix) {
        if (ehEmail(chavePix) || ehCpf(chavePix) || ehChaveAleatoria(chavePix)) {
            return chavePix;
        }

        return chavePix;
    }

    private boolean ehEmail(String chavePix) {
        return Email.formatoEmailEhValido(chavePix);
    }


    private boolean ehCpf(String chavePix) {
        return Cpf.formatoCpfEhValido(chavePix);
    }


    private boolean ehChaveAleatoria(String chavePix) {
        return PADRAO_CHAVE_ALEATORIA.matcher(chavePix).matches();
    }
}
