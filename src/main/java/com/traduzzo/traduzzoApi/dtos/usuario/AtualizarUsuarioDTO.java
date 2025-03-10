package com.traduzzo.traduzzoApi.dtos.usuario;

import com.traduzzo.traduzzoApi.entidades.usuario.PerfilDoUsuario;

public record AtualizarUsuarioDTO(String email, String senha, PerfilDoUsuario perfil) {
}
