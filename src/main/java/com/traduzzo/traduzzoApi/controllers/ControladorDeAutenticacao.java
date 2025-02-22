package com.traduzzo.traduzzoApi.controllers;

import com.traduzzo.traduzzoApi.dtos.autenticacao.AutenticacaoDTO;
import com.traduzzo.traduzzoApi.dtos.autenticacao.AutenticacaoRespostaDTO;
import com.traduzzo.traduzzoApi.entities.user.EntidadeUsuario;
import com.traduzzo.traduzzoApi.services.ServicoDeToken;
import com.traduzzo.traduzzoApi.services.ServicoDeUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("autenticacao")
public class ControladorDeAutenticacao {

    private final ServicoDeUsuario servicoDeUsuario;
    private final AuthenticationManager authenticationManager;
    private final ServicoDeToken servicoDeToken;

    @Autowired
    public ControladorDeAutenticacao(ServicoDeUsuario servicoDeUsuario, AuthenticationManager authenticationManager,
                                     ServicoDeToken servicoDeToken) {
        this.servicoDeUsuario = servicoDeUsuario;
        this.authenticationManager = authenticationManager;
        this.servicoDeToken = servicoDeToken;
    }


    @PostMapping("/login")
    public ResponseEntity<AutenticacaoRespostaDTO> login(@RequestBody AutenticacaoDTO autenticacaoDTO) {
        Authentication usuarioAutenticado = servicoDeUsuario.autenticarUsuario(autenticacaoDTO, authenticationManager);

        String token = servicoDeToken.gerarTokenDoUsuario((EntidadeUsuario) usuarioAutenticado.getPrincipal());

        return ResponseEntity.ok(new AutenticacaoRespostaDTO(token));
    }
}
