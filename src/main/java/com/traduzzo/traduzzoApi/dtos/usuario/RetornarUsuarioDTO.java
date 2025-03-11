package com.traduzzo.traduzzoApi.dtos.usuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.traduzzo.traduzzoApi.entidades.usuario.PerfilDoUsuario;
import com.traduzzo.traduzzoApi.objetosDeValor.Cpf;
import com.traduzzo.traduzzoApi.objetosDeValor.DadosBancarios;
import com.traduzzo.traduzzoApi.objetosDeValor.Email;
import com.traduzzo.traduzzoApi.objetosDeValor.Endereco;
import com.traduzzo.traduzzoApi.objetosDeValor.NomeCompleto;
import com.traduzzo.traduzzoApi.objetosDeValor.Telefone;

import java.time.LocalDate;
import java.util.Optional;

public record RetornarUsuarioDTO(
        Long id,
        Email email,
        Cpf cpf,
        NomeCompleto nomeCompleto,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Optional<LocalDate> dataNascimento,
        Telefone telefone,
        Optional<Endereco> endereco,
        Optional<DadosBancarios> dadosBancarios,
        PerfilDoUsuario perfil
) { }
