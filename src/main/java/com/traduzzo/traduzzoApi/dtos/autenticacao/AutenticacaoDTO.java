package com.traduzzo.traduzzoApi.dtos.autenticacao;

import com.traduzzo.traduzzoApi.objetosDeValor.Email;
import com.traduzzo.traduzzoApi.objetosDeValor.Senha;

public record AutenticacaoDTO(Email email, Senha senha) {
}
