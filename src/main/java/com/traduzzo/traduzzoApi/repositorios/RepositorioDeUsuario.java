package com.traduzzo.traduzzoApi.repositorios;

import com.traduzzo.traduzzoApi.entidades.usuario.EntidadeUsuario;
import com.traduzzo.traduzzoApi.objetosDeValor.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RepositorioDeUsuario extends JpaRepository<EntidadeUsuario, Long> {
    Optional<UserDetails> findByEmail(Email email);
}
