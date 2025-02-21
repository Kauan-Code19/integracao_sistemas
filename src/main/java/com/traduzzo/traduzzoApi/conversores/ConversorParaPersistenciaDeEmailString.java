package com.traduzzo.traduzzoApi.conversores;

import com.traduzzo.traduzzoApi.objetosDeValor.Email;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConversorParaPersistenciaDeEmailString implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {
        return email != null ? email.getValor() : null;
    }

    @Override
    public Email convertToEntityAttribute(String emailString) {
        return emailString != null ? new Email(emailString) : null;
    }
}
