package com.integracao.IntegracaoSistemaApi.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.Instant;

@AllArgsConstructor
@Getter
public class ErroPersonalizadoDTO {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String trace;
}
