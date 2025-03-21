package com.integracao.IntegracaoSistemaApi.controladores;

import com.integracao.IntegracaoSistemaApi.dtos.usuario.AtualizarUsuarioDTO;
import com.integracao.IntegracaoSistemaApi.dtos.usuario.RetornarUsuarioDTO;
import com.integracao.IntegracaoSistemaApi.dtos.usuario.RegistrarUsuarioDTO;
import com.integracao.IntegracaoSistemaApi.servicos.ServicoDeUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirUsuarioPorId(@PathVariable Long id) {
        servicoDeUsuario.excluirUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }
}
