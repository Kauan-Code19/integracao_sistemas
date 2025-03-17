package com.integracao.IntegracaoSistemaApi.conversores;

import com.integracao.IntegracaoSistemaApi.objetosDeValor.Senha;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConversorParaPersistenciaDeSenhaString implements AttributeConverter<Senha, String> {

    @Override
    public String convertToDatabaseColumn(Senha senha) {
        return senha != null ? senha.getValor() : null;
    }

    @Override
    public Senha convertToEntityAttribute(String senhaString) {
        return senhaString != null ? new Senha(senhaString) : null;
    }
}
