package com.traduzzo.traduzzoApi.servicos;

import com.traduzzo.traduzzoApi.dtos.autenticacao.AutenticacaoDTO;
import com.traduzzo.traduzzoApi.dtos.usuario.AtualizarUsuarioDTO;
import com.traduzzo.traduzzoApi.dtos.usuario.RegistrarUsuarioDTO;
import com.traduzzo.traduzzoApi.dtos.usuario.RetornarUsuarioDTO;
import com.traduzzo.traduzzoApi.entidades.usuario.EntidadeUsuario;
import com.traduzzo.traduzzoApi.excecoes.EntityAlreadyPresentException;
import com.traduzzo.traduzzoApi.objetosDeValor.Email;
import com.traduzzo.traduzzoApi.objetosDeValor.Senha;
import com.traduzzo.traduzzoApi.repositorios.RepositorioDeUsuario;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ServicoDeUsuario implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final RepositorioDeUsuario repositorioDeUsuario;

    @Autowired
    public ServicoDeUsuario(PasswordEncoder passwordEncoder, RepositorioDeUsuario repositorioDeUsuario) {
        this.passwordEncoder = passwordEncoder;
        this.repositorioDeUsuario = repositorioDeUsuario;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repositorioDeUsuario
                .findByEmail(Email.converterDeString(email))
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o e-mail: " + email));
    }


    public Authentication autenticarUsuario(AutenticacaoDTO autenticacaoDTO,
                                            AuthenticationManager authenticationManager) {
        return authenticationManager.authenticate(criarTokenAutenticacao(autenticacaoDTO));
    }


    private UsernamePasswordAuthenticationToken criarTokenAutenticacao(AutenticacaoDTO autenticacaoDTO) {
        return new UsernamePasswordAuthenticationToken(
                autenticacaoDTO.email().getValor(),
                autenticacaoDTO.senha().getValor()
        );
    }


    @Transactional
    public RetornarUsuarioDTO registrarUsuario(RegistrarUsuarioDTO registrarUsuarioDTO) {
        verificarSeUsuarioJaExiste(registrarUsuarioDTO.email());

        EntidadeUsuario entidadeUsuario = criarEntidadeUsuario(registrarUsuarioDTO);
        repositorioDeUsuario.save(entidadeUsuario);

        return criarRespostaUsuario(entidadeUsuario);
    }


    private void verificarSeUsuarioJaExiste(Email email) {
        repositorioDeUsuario.findByEmail(email)
                .ifPresent(usuario -> {
                    throw new EntityAlreadyPresentException("E-mail já cadastrado!");
                });
    }


    private EntidadeUsuario criarEntidadeUsuario(RegistrarUsuarioDTO registrarUsuarioDTO) {
        return new EntidadeUsuario(
                registrarUsuarioDTO.email(),
                registrarUsuarioDTO.cpf(),
                criptografarSenha(registrarUsuarioDTO.senha().getValor()),
                registrarUsuarioDTO.nomeCompleto(),
                registrarUsuarioDTO.dataNascimento()
                        .map(this::formatarDataNascimento)
                        .orElse(null),
                registrarUsuarioDTO.telefone(),
                registrarUsuarioDTO.endereco().orElse(null),
                registrarUsuarioDTO.dadosBancarios().orElse(null),
                registrarUsuarioDTO.perfil()
        );
    }


    private LocalDate formatarDataNascimento(LocalDate dataNascimento) {
        String dataFormatada = dataNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return LocalDate.parse(dataFormatada, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }


    @Transactional
    public RetornarUsuarioDTO atualizarUsuarioPorId(Long id, AtualizarUsuarioDTO atualizarUsuarioDTO) {
        EntidadeUsuario entidadeUsuario = buscarUsuarioPorId(id);
        repositorioDeUsuario.save(setarNovosValoresEntidadeUsuario(entidadeUsuario, atualizarUsuarioDTO));

        return criarRespostaUsuario(entidadeUsuario);
    }


    public RetornarUsuarioDTO retornarUsuarioPorId(Long id) {
        EntidadeUsuario entidadeUsuario = buscarUsuarioPorId(id);
        return criarRespostaUsuario(entidadeUsuario);
    }


    @Transactional
    public void excluirUsuarioPorId(Long id) {
        buscarUsuarioPorId(id);
        repositorioDeUsuario.deleteById(id);
    }


    @Transactional(readOnly = true)
    private EntidadeUsuario buscarUsuarioPorId(Long id) {
        return repositorioDeUsuario.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuário não encontrado com o ID: " + id)
                );
    }


    private EntidadeUsuario setarNovosValoresEntidadeUsuario(
            EntidadeUsuario usuario,
            AtualizarUsuarioDTO atualizarUsuarioDTO
    ) {
        if (atualizarUsuarioDTO.email().isPresent()) {
            atualizarSeDiferente(
                    usuario::getEmail,
                    usuario::setEmail,
                    atualizarUsuarioDTO.email().get()
            );
        }

        if (atualizarUsuarioDTO.senha().isPresent()) {
            atualizarSenhaSeNecessario(
                    usuario,
                    atualizarUsuarioDTO.senha().get().getValor());
        }

        if (atualizarUsuarioDTO.cpf().isPresent()) {
            atualizarSeDiferente(
                    usuario::getCpf,
                    usuario::setCpf,
                    atualizarUsuarioDTO.cpf().get());
        }

        if (atualizarUsuarioDTO.nomeCompleto().isPresent()) {
            atualizarSeDiferente(
                    usuario::getNomeCompleto,
                    usuario::setNomeCompleto,
                    atualizarUsuarioDTO.nomeCompleto().get());
        }

        if (atualizarUsuarioDTO.dataNascimento().isPresent()) {
            LocalDate dataFormatada = formatarDataNascimento(atualizarUsuarioDTO.dataNascimento().get());
            atualizarSeDiferente(
                    usuario::getDataNascimento,
                    usuario::setDataNascimento,
                    dataFormatada
            );
        }

        if (atualizarUsuarioDTO.telefone().isPresent()) {
            atualizarSeDiferente(
                    usuario::getTelefone,
                    usuario::setTelefone,
                    atualizarUsuarioDTO.telefone().get()
            );
        }

        if (atualizarUsuarioDTO.endereco().isPresent()) {
            atualizarSeDiferente(
                    usuario::getEndereco,
                    usuario::setEndereco,
                    atualizarUsuarioDTO.endereco().get()
            );
        }

        if (atualizarUsuarioDTO.dadosBancarios().isPresent()) {
            atualizarSeDiferente(
                    usuario::getDadosBancarios,
                    usuario::setDadosBancarios,
                    atualizarUsuarioDTO.dadosBancarios().get()
            );
        }

        atualizarSeDiferente(usuario::getPerfil, usuario::setPerfil, atualizarUsuarioDTO.perfil());

        return usuario;
    }


    private void atualizarSenhaSeNecessario(EntidadeUsuario usuario, String senha) {
        if (senha != null && !senha.isBlank()) {
            Senha senhaConvertida = Senha.converterDeString(senha);
            usuario.setSenha(criptografarSenha(senhaConvertida.getValor()));
        }
    }


    private <T> void atualizarSeDiferente(Supplier<T> getter, Consumer<T> setter, T novoValor) {
        if (!Objects.equals(getter.get(), novoValor)) {
            setter.accept(novoValor);
        }
    }


    private Senha criptografarSenha(String senha) {
        String senhaHash = passwordEncoder.encode(senha);
        return Senha.converterDeString(senhaHash);
    }


    private RetornarUsuarioDTO criarRespostaUsuario(EntidadeUsuario entidadeusuario) {
        return new RetornarUsuarioDTO(
                entidadeusuario.getId(),
                entidadeusuario.getEmail(),
                entidadeusuario.getCpf(),
                entidadeusuario.getNomeCompleto(),
                Optional.ofNullable(entidadeusuario.getDataNascimento()),
                entidadeusuario.getTelefone(),
                Optional.ofNullable(entidadeusuario.getEndereco()),
                Optional.ofNullable(entidadeusuario.getDadosBancarios()),
                entidadeusuario.getPerfil()
        );
    }


    @Transactional(readOnly = true)
    public List<RetornarUsuarioDTO> retornarTodosUsuarios() {
        return repositorioDeUsuario.findAll()
                .stream()
                .map(this::criarRespostaUsuario)
                .collect(Collectors.toList());
    }
}
