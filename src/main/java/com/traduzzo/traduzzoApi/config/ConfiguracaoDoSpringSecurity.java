package com.traduzzo.traduzzoApi.config;

import com.traduzzo.traduzzoApi.FiltroDeAutenticacaoPorToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ConfiguracaoDoSpringSecurity {

    private final FiltroDeAutenticacaoPorToken filtroDeAutenticacaoPorToken;

    @Autowired
    public ConfiguracaoDoSpringSecurity(FiltroDeAutenticacaoPorToken filtroDeAutenticacaoPorToken) {
        this.filtroDeAutenticacaoPorToken = filtroDeAutenticacaoPorToken;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(this::configurarGerenciamentoDeSessao)
                .authorizeHttpRequests(this::configurarAutorizacoes)
                .addFilterBefore(filtroDeAutenticacaoPorToken, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    private void configurarGerenciamentoDeSessao(SessionManagementConfigurer<HttpSecurity> session) {
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    private void configurarAutorizacoes(AuthorizeHttpRequestsConfigurer<HttpSecurity>
                                                .AuthorizationManagerRequestMatcherRegistry authorize) {
        authorize
                .requestMatchers(HttpMethod.POST, "/autenticacao/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuario/registrar").hasRole("ADMINISTRADOR")
                .anyRequest().authenticated();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
