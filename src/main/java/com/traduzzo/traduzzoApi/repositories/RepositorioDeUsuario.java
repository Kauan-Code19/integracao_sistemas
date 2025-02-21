package com.traduzzo.traduzzoApi.repositories;

import com.traduzzo.traduzzoApi.entities.user.EntidadeUsuario;
import com.traduzzo.traduzzoApi.objetosDeValor.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDeUsuario extends JpaRepository<EntidadeUsuario, Long> {
    UserDetails findByEmail(Email email);
}
