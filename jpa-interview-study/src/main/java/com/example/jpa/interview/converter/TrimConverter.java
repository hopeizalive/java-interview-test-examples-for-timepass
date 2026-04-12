package com.example.jpa.interview.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TrimConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute == null ? null : attribute.trim();
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData == null ? null : dbData.trim();
    }
}
