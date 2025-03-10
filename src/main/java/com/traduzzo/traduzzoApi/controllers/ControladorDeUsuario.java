package com.traduzzo.traduzzoApi.controllers;

import com.traduzzo.traduzzoApi.dtos.usuario.AtualizarUsuarioDTO;
import com.traduzzo.traduzzoApi.dtos.usuario.RetornarUsuarioDTO;
import com.traduzzo.traduzzoApi.dtos.usuario.RegistrarUsuarioDTO;
import com.traduzzo.traduzzoApi.services.ServicoDeUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("usuario")
public class ControladorDeUsuario {

    private final ServicoDeUsuario servicoDeUsuario;

    @Autowired
    public ControladorDeUsuario(ServicoDeUsuario servicoDeUsuario) {
        this.servicoDeUsuario = servicoDeUsuario;
    }


    @PostMapping("/registrar")
    public ResponseEntity<RetornarUsuarioDTO> registrarUsuario(
            @RequestBody RegistrarUsuarioDTO registrarUsuarioDTO
    ) {
        RetornarUsuarioDTO autenticacaoRespostaDTO = servicoDeUsuario.registrarUsuario(registrarUsuarioDTO);
        URI uri = criarURI(autenticacaoRespostaDTO.id());

        return ResponseEntity.created(uri).body(autenticacaoRespostaDTO);
    }


    private URI criarURI(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }


    @GetMapping("/listar")
    public ResponseEntity<List<RetornarUsuarioDTO>> retornarTodosUsuarios() {
        List<RetornarUsuarioDTO> usuarioDTOS = servicoDeUsuario.retornarTodosUsuarios();
        return ResponseEntity.ok(usuarioDTOS);
    }


    @GetMapping("/{id}")
    public ResponseEntity<RetornarUsuarioDTO> retornarUsuarioPorId(@PathVariable Long id){
        RetornarUsuarioDTO usuarioDTO = servicoDeUsuario.retornarUsuarioPorId(id);
        return ResponseEntity.ok(usuarioDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<RetornarUsuarioDTO> atualizarUsuarioPorId(
            @PathVariable Long id,
            @RequestBody AtualizarUsuarioDTO atualizarUsuarioDTO
    ) {
        RetornarUsuarioDTO usuarioDTO = servicoDeUsuario.atualizarUsuarioPorId(id, atualizarUsuarioDTO);
        return ResponseEntity.ok(usuarioDTO);
    }
}
