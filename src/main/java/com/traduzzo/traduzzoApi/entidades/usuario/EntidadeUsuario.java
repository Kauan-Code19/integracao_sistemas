package com.traduzzo.traduzzoApi.entidades.usuario;

import com.traduzzo.traduzzoApi.conversores.ConversorParaPersistenciaDeCpfString;
import com.traduzzo.traduzzoApi.conversores.ConversorParaPersistenciaDeEmailString;
import com.traduzzo.traduzzoApi.conversores.ConversorParaPersistenciaDeNomeCompletoString;
import com.traduzzo.traduzzoApi.conversores.ConversorParaPersistenciaDeSenhaString;
import com.traduzzo.traduzzoApi.conversores.ConversorParaPersistenciaDeTelefoneString;
import com.traduzzo.traduzzoApi.objetosDeValor.Cpf;
import com.traduzzo.traduzzoApi.objetosDeValor.DadosBancarios;
import com.traduzzo.traduzzoApi.objetosDeValor.Email;
import com.traduzzo.traduzzoApi.objetosDeValor.Endereco;
import com.traduzzo.traduzzoApi.objetosDeValor.NomeCompleto;
import com.traduzzo.traduzzoApi.objetosDeValor.Senha;
import com.traduzzo.traduzzoApi.objetosDeValor.Telefone;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
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
import java.time.LocalDate;
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

    @Column(name = "cpf", nullable = false, updatable = false, unique = true)
    @Convert(converter = ConversorParaPersistenciaDeCpfString.class)
    private Cpf cpf;

    @Column(name = "nome_completo", nullable = false, unique = true)
    @Convert(converter = ConversorParaPersistenciaDeNomeCompletoString.class)
    private NomeCompleto nomeCompleto;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "telefone", nullable = false, unique = true)
    @Convert(converter = ConversorParaPersistenciaDeTelefoneString.class)
    private Telefone telefone;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "logradouro", column = @Column(name = "logradouro")),
            @AttributeOverride(name = "numero", column = @Column(name = "numero")),
            @AttributeOverride(name = "bairro", column = @Column(name = "bairro")),
            @AttributeOverride(name = "cep", column = @Column(name = "cep")),
            @AttributeOverride(name = "cidade", column = @Column(name = "cidade")),
            @AttributeOverride(name = "estado", column = @Column(name = "estado"))
    })
    private Endereco endereco;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "banco", column = @Column(name = "banco_usuario")),
            @AttributeOverride(name = "conta", column = @Column(name = "conta_bancaria")),
            @AttributeOverride(name = "agencia", column = @Column(name = "agencia_bancaria")),
            @AttributeOverride(name = "chavePix", column = @Column(name = "chave_pix_usuario"))
    })
    private DadosBancarios dadosBancarios;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    private PerfilDoUsuario perfil;


    public EntidadeUsuario(
            Email email,
            Cpf cpf,
            Senha senha,
            NomeCompleto nomeCompleto,
            LocalDate dataNascimento,
            Telefone telefone,
            Endereco endereco,
            DadosBancarios dadosBancarios,
            PerfilDoUsuario perfil
    ) {
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.endereco = endereco;
        this.dadosBancarios = dadosBancarios;
        this.perfil = perfil;
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
