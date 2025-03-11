package com.traduzzo.traduzzoApi.conversores;

import com.traduzzo.traduzzoApi.objetosDeValor.Cpf;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConversorParaPersistenciaDeCpfString implements AttributeConverter<Cpf, String> {
    @Override
    public String convertToDatabaseColumn(Cpf cpf) {
        return cpf != null ? cpf.getValor() : null;
    }

    @Override
    public Cpf convertToEntityAttribute(String cpfString) {
        return cpfString != null ? new Cpf(cpfString) : null;
    }
}
