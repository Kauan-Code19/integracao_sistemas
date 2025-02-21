package com.traduzzo.traduzzoApi.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.traduzzo.traduzzoApi.entities.user.EntidadeUsuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class ServicoDeToken {

    @Value("${api.security.token.secret}")
    private String segredo;

    public String gerarTokenDoUsuario(EntidadeUsuario entidadeUsuario) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(segredo);
            return JWT.create()
                    .withIssuer("traduzzo-api")
                    .withSubject(entidadeUsuario.getEmail().getValor())
                    .withClaim("role", entidadeUsuario.getPerfil().name())
                    .withExpiresAt(gerarTempoDeExpiracaoDoToken())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }


    private Instant gerarTempoDeExpiracaoDoToken() {
        return LocalDateTime.now().plusMinutes(25).toInstant(ZoneOffset.of("-03:00"));
    }


    public String validarTokenDoUsuario(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(segredo);

            return JWT.require(algorithm)
                    .withIssuer("traduzzo-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }
}
