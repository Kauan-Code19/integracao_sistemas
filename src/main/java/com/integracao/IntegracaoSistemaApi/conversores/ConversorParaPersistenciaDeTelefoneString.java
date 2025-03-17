package com.integracao.IntegracaoSistemaApi.conversores;

import com.integracao.IntegracaoSistemaApi.objetosDeValor.Telefone;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConversorParaPersistenciaDeTelefoneString implements AttributeConverter<Telefone, String> {

    @Override
    public String convertToDatabaseColumn(Telefone telefone) {
        return telefone != null ? telefone.getValor() : null;
    }

    @Override
    public Telefone convertToEntityAttribute(String telefoneString) {
        return telefoneString != null ? new Telefone(telefoneString) : null;
    }
}