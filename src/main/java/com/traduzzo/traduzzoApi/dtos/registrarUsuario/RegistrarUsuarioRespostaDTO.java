package com.traduzzo.traduzzoApi.dtos.registrarUsuario;

import com.traduzzo.traduzzoApi.entities.user.PerfilDoUsuario;
import com.traduzzo.traduzzoApi.objetosDeValor.Email;

public record RegistrarUsuarioRespostaDTO(Long id, Email email, PerfilDoUsuario perfil) {
}
