package com.traduzzo.traduzzoApi.controllers;

import com.traduzzo.traduzzoApi.dtos.ErroPersonalizadoDTO;
import com.traduzzo.traduzzoApi.excecoes.EntityAlreadyPresentException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;

@ControllerAdvice
public class ControladorDeManipuladorDeExcecoes {

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


    private ResponseEntity<ErroPersonalizadoDTO> criarRespostaDeErro(Exception exception, HttpStatus status,
                                                                     HttpServletRequest request) {
        ErroPersonalizadoDTO erroPersonalizadoDTO = new ErroPersonalizadoDTO(
                Instant.now(), status.value(), exception.getMessage(), request.getRequestURI()
        );

        return ResponseEntity.status(status).body(erroPersonalizadoDTO);
    }
}