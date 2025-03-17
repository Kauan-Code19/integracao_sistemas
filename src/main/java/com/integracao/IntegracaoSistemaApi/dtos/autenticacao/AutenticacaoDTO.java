package com.integracao.IntegracaoSistemaApi.dtos.autenticacao;

import com.integracao.IntegracaoSistemaApi.objetosDeValor.Email;
import com.integracao.IntegracaoSistemaApi.objetosDeValor.Senha;

public record AutenticacaoDTO(Email email, Senha senha) {
}
