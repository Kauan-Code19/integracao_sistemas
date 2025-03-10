package com.traduzzo.traduzzoApi.entities.user;

import com.traduzzo.traduzzoApi.conversores.ConversorParaPersistenciaDeEmailString;
import com.traduzzo.traduzzoApi.conversores.ConversorParaPersistenciaDeSenhaString;
import com.traduzzo.traduzzoApi.objetosDeValor.Email;
import com.traduzzo.traduzzoApi.objetosDeValor.Senha;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class EntidadeUsuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "email", nullable = false)
    @Convert(converter = ConversorParaPersistenciaDeEmailString.class)
    private Email email;

    @Column(name = "senha", nullable = false)
    @Convert(converter = ConversorParaPersistenciaDeSenhaString.class)
    private Senha senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    private PerfilDoUsuario perfil;


    public EntidadeUsuario(Email email, PerfilDoUsuario perfil, Senha senha) {
        this.email = email;
        this.perfil = perfil;
        this.senha = senha;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.perfil.name()));

        if (this.perfil == PerfilDoUsuario.ADMINISTRADOR) {
            authorities.add(new SimpleGrantedAuthority("ROLE_COMERCIAL"));
            authorities.add(new SimpleGrantedAuthority("ROLE_PROJETOS"));
        }

        return authorities;
    }


    @Override
    public String getPassword() {
        return senha.getValor();
    }


    @Override
    public String getUsername() {
        return email.getValor();
    }


    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }


    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }


    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
