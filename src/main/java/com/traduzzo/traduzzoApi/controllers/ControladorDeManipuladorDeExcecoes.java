package com.traduzzo.traduzzoApi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.traduzzo.traduzzoApi.dtos.ErroPersonalizadoDTO;
import com.traduzzo.traduzzoApi.excecoes.EntityAlreadyPresentException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.io.IOException;
import java.time.Instant;

@ControllerAdvice
public class ControladorDeManipuladorDeExcecoes implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroPersonalizadoDTO> illegalArgumentException(IllegalArgumentException exception,
                                                                         HttpServletRequest request) {
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


    private ResponseEntity<ErroPersonalizadoDTO> criarRespostaDeErro(Exception exception, HttpStatus status,
                                                                     HttpServletRequest request) {
        ErroPersonalizadoDTO erroPersonalizadoDTO = new ErroPersonalizadoDTO(
                Instant.now(), status.value(), exception.getMessage(), request.getRequestURI()
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