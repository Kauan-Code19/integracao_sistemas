package com.traduzzo.traduzzoApi.dtos.usuario;

import com.traduzzo.traduzzoApi.entidades.usuario.PerfilDoUsuario;
import com.traduzzo.traduzzoApi.objetosDeValor.Email;

public record RetornarUsuarioDTO(Long id, Email email, PerfilDoUsuario perfil) {
}
