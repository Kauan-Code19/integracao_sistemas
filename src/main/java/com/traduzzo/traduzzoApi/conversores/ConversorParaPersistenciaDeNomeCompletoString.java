package com.traduzzo.traduzzoApi.conversores;

import com.traduzzo.traduzzoApi.objetosDeValor.NomeCompleto;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConversorParaPersistenciaDeNomeCompletoString implements AttributeConverter<NomeCompleto, String> {

    @Override
    public String convertToDatabaseColumn(NomeCompleto nomeCompleto) {
        return nomeCompleto != null ? nomeCompleto.getValor() : null;
    }

    @Override
    public NomeCompleto convertToEntityAttribute(String nomeCompletoString) {
        return nomeCompletoString != null ? new NomeCompleto(nomeCompletoString) : null;
    }
}
