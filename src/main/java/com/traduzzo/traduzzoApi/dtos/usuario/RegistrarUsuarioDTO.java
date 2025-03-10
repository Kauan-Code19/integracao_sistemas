package com.traduzzo.traduzzoApi.dtos.usuario;

import com.traduzzo.traduzzoApi.entities.user.PerfilDoUsuario;
import com.traduzzo.traduzzoApi.objetosDeValor.Email;
import com.traduzzo.traduzzoApi.objetosDeValor.Senha;

public record RegistrarUsuarioDTO(Email email, Senha senha, PerfilDoUsuario perfil) {
}
