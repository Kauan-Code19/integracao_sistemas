package com.integracao.IntegracaoSistemaApi.dtos.usuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.integracao.IntegracaoSistemaApi.entidades.usuario.PerfilDoUsuario;
import com.integracao.IntegracaoSistemaApi.objetosDeValor.Cpf;
import com.integracao.IntegracaoSistemaApi.objetosDeValor.DadosBancarios;
import com.integracao.IntegracaoSistemaApi.objetosDeValor.Email;
import com.integracao.IntegracaoSistemaApi.objetosDeValor.Endereco;
import com.integracao.IntegracaoSistemaApi.objetosDeValor.NomeCompleto;
import com.integracao.IntegracaoSistemaApi.objetosDeValor.Senha;
import com.integracao.IntegracaoSistemaApi.objetosDeValor.Telefone;
import java.time.LocalDate;
import java.util.Optional;

public record AtualizarUsuarioDTO(
        Optional<Email> email,
        Optional<Senha> senha,
        Optional<Cpf> cpf,
        Optional<NomeCompleto> nomeCompleto,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Optional<LocalDate> dataNascimento,
        Optional<Telefone> telefone,
        Optional<Endereco> endereco,
        Optional<DadosBancarios> dadosBancarios,
        PerfilDoUsuario perfil
) { }
