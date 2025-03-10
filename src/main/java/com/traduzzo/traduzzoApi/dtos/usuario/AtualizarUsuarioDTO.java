package com.traduzzo.traduzzoApi.dtos.usuario;

import com.traduzzo.traduzzoApi.entities.user.PerfilDoUsuario;

public record AtualizarUsuarioDTO(String email, String senha, PerfilDoUsuario perfil) {
}
