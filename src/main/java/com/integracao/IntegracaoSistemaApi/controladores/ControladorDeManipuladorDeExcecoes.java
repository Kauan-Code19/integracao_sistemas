package com.integracao.IntegracaoSistemaApi.controladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.integracao.IntegracaoSistemaApi.dtos.ErroPersonalizadoDTO;
import com.integracao.IntegracaoSistemaApi.excecoes.EntityAlreadyPresentException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class ControladorDeManipuladorDeExcecoes implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final Map<Class<? extends Throwable>,
            BiFunction<Throwable, HttpServletRequest, ResponseEntity<ErroPersonalizadoDTO>>> handlers;

    public ControladorDeManipuladorDeExcecoes() {
        handlers = new HashMap<>();
        handlers.put(IllegalArgumentException.class, this::illegalArgument);
        handlers.put(InvalidFormatException.class, this::invalidFormat);
        handlers.put(DateTimeParseException.class, this::dateTimeParse);
        handlers.put(DataIntegrityViolationException.class, this::dataIntegrityViolation);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroPersonalizadoDTO> illegalArgument(Throwable exception,
                                                                         HttpServletRequest request) {
        return criarRespostaDeErro(exception.getMessage(), HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroPersonalizadoDTO> dataIntegrityViolation(Throwable exception,
                                                                         HttpServletRequest request) {
        String mensagemErro = extrairMensagemPrincipal(exception.getMessage());
        return criarRespostaDeErro(mensagemErro, HttpStatus.BAD_REQUEST, request);
    }


    private String extrairMensagemPrincipal(String mensagem) {
        if (mensagem == null) {
            return "Erro de integridade nos dados.";
        }

        Pattern pattern = Pattern.compile("\\[.*?(Chave.*?)]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(mensagem);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return "Erro de integridade nos dados.";
    }


    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErroPersonalizadoDTO> invalidFormat(
            Throwable exception, HttpServletRequest request) {
        return criarRespostaDeErro(exception, HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErroPersonalizadoDTO> dateTimeParse(
            Throwable exception, HttpServletRequest request) {
        return criarRespostaDeErro(exception, HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(EntityAlreadyPresentException.class)
    public ResponseEntity<ErroPersonalizadoDTO> entityReadyPresent(EntityAlreadyPresentException exception,
                                                             HttpServletRequest request) {
        return criarRespostaDeErro(exception, HttpStatus.CONFLICT, request);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroPersonalizadoDTO> entityNotFound(EntityNotFoundException exception,
                                                           HttpServletRequest request) {
        return criarRespostaDeErro(exception, HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroPersonalizadoDTO> badCredentialsException(BadCredentialsException exception,
                                                                        HttpServletRequest request) {
        return criarRespostaDeErro(exception, HttpStatus.UNAUTHORIZED, request);
    }


    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErroPersonalizadoDTO> authorizationDenied(AuthorizationDeniedException exception,
                                                                        HttpServletRequest request) {
        return criarRespostaDeErro(exception, HttpStatus.UNAUTHORIZED, request);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroPersonalizadoDTO> httpMessageNotReadableException(
            HttpMessageNotReadableException exception, HttpServletRequest request) {

        Throwable causaRaiz = exception.getMostSpecificCause();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        BiFunction<Throwable, HttpServletRequest, ResponseEntity<ErroPersonalizadoDTO>> handler =
                handlers.get(causaRaiz.getClass());

        return handler != null
                ? handler.apply(causaRaiz, request)
                : criarRespostaDeErro("Erro ao processar a requisição. Verifique o formato dos dados enviados.",
                status, request, causaRaiz);
    }


    private ResponseEntity<ErroPersonalizadoDTO> criarRespostaDeErro(Exception exception, HttpStatus status,
                                                                     HttpServletRequest request) {
        ErroPersonalizadoDTO erroPersonalizadoDTO = new ErroPersonalizadoDTO(
                Instant.now(), status.value(), exception.getMessage(), request.getRequestURI()
        );

        return ResponseEntity.status(status).body(erroPersonalizadoDTO);
    }


    private ResponseEntity<ErroPersonalizadoDTO> criarRespostaDeErro(Throwable exception, HttpStatus status,
                                                                     HttpServletRequest request) {
        ErroPersonalizadoDTO erroPersonalizadoDTO = new ErroPersonalizadoDTO(
                Instant.now(), status.value(), exception.getMessage(), request.getRequestURI()
        );

        return ResponseEntity.status(status).body(erroPersonalizadoDTO);
    }


    private ResponseEntity<ErroPersonalizadoDTO> criarRespostaDeErro(
            String mensagem,
            HttpStatus status,
            HttpServletRequest request
    ) {
        ErroPersonalizadoDTO erroPersonalizadoDTO = new ErroPersonalizadoDTO(
                Instant.now(), status.value(), mensagem, request.getRequestURI()
        );

        return ResponseEntity.status(status).body(erroPersonalizadoDTO);
    }


    private ResponseEntity<ErroPersonalizadoDTO> criarRespostaDeErro(
            String mensagem,
            HttpStatus status,
            HttpServletRequest request,
            Throwable causaRaiz) {
        String detalhes = causaRaiz != null ? mensagem + " - Causa: " +
                causaRaiz.getClass().getSimpleName() : mensagem;

        ErroPersonalizadoDTO erroPersonalizadoDTO = new ErroPersonalizadoDTO(
                Instant.now(), status.value(), detalhes, request.getRequestURI()
        );

        return ResponseEntity.status(status).body(erroPersonalizadoDTO);
    }


    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException
    ) throws IOException, ServletException {
        ErroPersonalizadoDTO erroPersonalizadoDTO = new ErroPersonalizadoDTO(
                Instant.now(), HttpStatus.UNAUTHORIZED.value(),
                "Acesso negado! Autenticação e Autorização necessária.",
                request.getRequestURI()
        );

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(erroPersonalizadoDTO));
    }
}