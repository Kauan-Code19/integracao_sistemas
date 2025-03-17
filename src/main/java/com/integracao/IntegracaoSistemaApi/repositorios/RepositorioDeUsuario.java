package com.integracao.IntegracaoSistemaApi.repositorios;

import com.integracao.IntegracaoSistemaApi.entidades.usuario.EntidadeUsuario;
import com.integracao.IntegracaoSistemaApi.objetosDeValor.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RepositorioDeUsuario extends JpaRepository<EntidadeUsuario, Long> {
    Optional<UserDetails> findByEmail(Email email);
}
