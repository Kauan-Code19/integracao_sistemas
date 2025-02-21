package com.traduzzo.traduzzoApi;

import com.traduzzo.traduzzoApi.objetosDeValor.Email;
import com.traduzzo.traduzzoApi.repositories.RepositorioDeUsuario;
import com.traduzzo.traduzzoApi.services.ServicoDeToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class FiltroDeAutenticacaoPorToken extends OncePerRequestFilter {

    private final ServicoDeToken servicoDeToken;
    private final RepositorioDeUsuario repositorioDeUsuario;

    @Autowired
    public FiltroDeAutenticacaoPorToken(ServicoDeToken servicoDeToken, RepositorioDeUsuario repositorioDeUsuario) {
        this.servicoDeToken = servicoDeToken;
        this.repositorioDeUsuario = repositorioDeUsuario;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = this.recuperarToken(request);

        if (token != null) {
            Authentication autenticacao = obterAutenticacao(token);
            configurarAutenticacao(autenticacao);
        }

        filterChain.doFilter(request, response);
    }


    private String recuperarToken(HttpServletRequest request) {
        var cabecalhoAutenticacao = request.getHeader("Authorization");

        if (cabecalhoAutenticacao == null) {
            return  null;
        }

        return cabecalhoAutenticacao.replace("Bearer ", "");
    }


    private Authentication obterAutenticacao(String token) {
        String subject = servicoDeToken.validarTokenDoUsuario(token);
        UserDetails userDetails = repositorioDeUsuario.findByEmail(Email.converterDeString(subject));

        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
    }


    private void configurarAutenticacao(Authentication autenticacao) {
        SecurityContextHolder.getContext().setAuthentication(autenticacao);
    }
}
