package com.traduzzo.traduzzoApi.controllers;

import com.traduzzo.traduzzoApi.dtos.registrarUsuario.RegistrarUsuarioRespostaDTO;
import com.traduzzo.traduzzoApi.dtos.registrarUsuario.RegistrarUsuarioDTO;
import com.traduzzo.traduzzoApi.services.ServicoDeUsuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("usuario")
public class ControladorDeUsuario {

    private final ServicoDeUsuario servicoDeUsuario;

    @Autowired
    public ControladorDeUsuario(ServicoDeUsuario servicoDeUsuario) {
        this.servicoDeUsuario = servicoDeUsuario;
    }


    @PostMapping("/registrar")
    public ResponseEntity<RegistrarUsuarioRespostaDTO> registrarUsuario(
            @Valid @RequestBody RegistrarUsuarioDTO registrarUsuarioDTO
    ) {
        RegistrarUsuarioRespostaDTO autenticacaoRespostaDTO = servicoDeUsuario.registrarUsuario(registrarUsuarioDTO);

        URI uri = criarURI(autenticacaoRespostaDTO.id());

        return ResponseEntity.created(uri).body(autenticacaoRespostaDTO);
    }


    private URI criarURI(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
