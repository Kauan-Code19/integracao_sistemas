package com.traduzzo.traduzzoApi.services;

import com.traduzzo.traduzzoApi.dtos.autenticacao.AutenticacaoDTO;
import com.traduzzo.traduzzoApi.dtos.registrarUsuario.RegistrarUsuarioRespostaDTO;
import com.traduzzo.traduzzoApi.dtos.registrarUsuario.RegistrarUsuarioDTO;
import com.traduzzo.traduzzoApi.entities.user.EntidadeUsuario;
import com.traduzzo.traduzzoApi.objetosDeValor.Email;
import com.traduzzo.traduzzoApi.objetosDeValor.Senha;
import com.traduzzo.traduzzoApi.repositories.RepositorioDeUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicoDeUsuario implements UserDetailsService {

    private final RepositorioDeUsuario repositorioDeUsuario;

    @Autowired
    public ServicoDeUsuario(RepositorioDeUsuario repositorioDeUsuario) {
        this.repositorioDeUsuario = repositorioDeUsuario;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repositorioDeUsuario.findByEmail(Email.converterDeString(username));
    }


    public Authentication autenticarUsuario(AutenticacaoDTO autenticacaoDTO,
                                            AuthenticationManager authenticationManager) {
        return authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(autenticacaoDTO.email().getValor(),
                                autenticacaoDTO.senha().getValor())
                );
    }


    @Transactional
    public RegistrarUsuarioRespostaDTO registrarUsuario(RegistrarUsuarioDTO registrarUsuarioDTO) {
        verificarSeUsuarioJaExiste(registrarUsuarioDTO.email());

        EntidadeUsuario entidadeUsuario = criarEntidadeUsuario(registrarUsuarioDTO);
        repositorioDeUsuario.save(entidadeUsuario);

        return criarRespostaAutenticacao(entidadeUsuario);
    }


    private void verificarSeUsuarioJaExiste(Email email) {
        if (repositorioDeUsuario.findByEmail(email) != null) {
            throw new RuntimeException("E-mail já cadastrado!");
        }
    }


    private EntidadeUsuario criarEntidadeUsuario(RegistrarUsuarioDTO registrarUsuarioDTO) {
        String senhaHash = new BCryptPasswordEncoder().encode(registrarUsuarioDTO.senha().getValor());

        EntidadeUsuario usuario = new EntidadeUsuario();
        usuario.setEmail(registrarUsuarioDTO.email());
        usuario.setPerfil(registrarUsuarioDTO.perfil());
        usuario.setSenha(Senha.converterDeString(senhaHash));

        return usuario;
    }


    private RegistrarUsuarioRespostaDTO criarRespostaAutenticacao(EntidadeUsuario Entidadeusuario) {
        return new RegistrarUsuarioRespostaDTO(
                Entidadeusuario.getId(), Entidadeusuario.getEmail(), Entidadeusuario.getPerfil()
        );
    }

}
