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
    private static final Pattern FORMATO_AGENCIA = Pattern.compile("^\\d{0,12}(-\\d{0,2})?$");
    private static final Pattern FORMATO_CONTA = Pattern.compile("^\\d{0,12}(-\\d{0,2})?$");
    private static final Pattern PADRAO_CHAVE_ALEATORIA = Pattern.compile(
            "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"
    );
    private static final int TAMANHO_MIN_CONTA = 1;
    private static final int TAMANHO_MAX_CONTA = 12;
    private static final int TAMANHO_MIN_AGENCIA = 1;
    private static final int TAMANHO_MAX_AGENCIA = 5;

    private final String banco;
    private final String conta;
    private final String agencia;
    private final String chavePix;

    @JsonCreator
    public DadosBancarios(String banco, String conta, String agencia, String chavePix) {
        validarPreenchimentoCamposBancarios(banco, conta, agencia,chavePix);

        this.banco = banco;
        this.conta = campoPreenchido(conta) ? verificarSeContaTemFormatoValido(conta) : null;
        this.agencia = campoPreenchido(agencia) ? verificarSeAgenciaTemFormatoValido(agencia) : null;
        this.chavePix = campoPreenchido(chavePix) ? validarTipoDeChave(chavePix) : null;
    }


    private void validarPreenchimentoCamposBancarios(
            String banco,
            String conta,
            String agencia,
            String chavePix
    ) {
        boolean bancoPreenchido = campoPreenchido(banco);
        boolean contaPreenchida = campoPreenchido(conta);
        boolean agenciaPreenchida = campoPreenchido(agencia);

        boolean algumPreenchido = bancoPreenchido || contaPreenchida || agenciaPreenchida;
        boolean todosPreenchidos = bancoPreenchido && contaPreenchida && agenciaPreenchida;

        if (algumPreenchido && !todosPreenchidos) {
            throw new IllegalArgumentException(
                    "Banco, conta e agência devem estar todos preenchidos ou todos vazios."
            );
        }

        if (campoBranco(banco) || campoBranco(conta) || campoBranco(agencia) || campoBranco(chavePix)) {
            throw new IllegalArgumentException("Os campos de dados bancarios não podem estar em branco.");
        }
    }


    private boolean campoBranco(String campo) {
        return campo != null && campo.isBlank();
    }


    private boolean campoPreenchido(String campo) {
        return campo != null && !campo.isBlank();
    }


    private String verificarSeContaTemFormatoValido(String conta) {
        if (!FORMATO_CONTA.matcher(conta).matches()) {
            throw new IllegalArgumentException(
                    "A conta deve ter entre " + TAMANHO_MIN_CONTA +
                            " e " + TAMANHO_MAX_CONTA + " caracteres podendo" +
                            " conter um dígito" + " de 0 a 2 caracteres separado por hífen"
            );
        }

        return conta;
    }


    private String verificarSeAgenciaTemFormatoValido(String agencia) {
        if (!FORMATO_AGENCIA.matcher(agencia).matches()) {
            throw new IllegalArgumentException(
                    "A agência deve ter entre " + TAMANHO_MIN_AGENCIA +
                            " e " + TAMANHO_MAX_AGENCIA + " números, podendo" +
                            " conter um dígito de 0 a 2 caracteres separado por hífen"
            );
        }

        return agencia;
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
