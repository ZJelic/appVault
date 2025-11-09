package com.project.appvault.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
import java.util.Map;

@Converter(autoApply = true)
public class ListMapToJsonConverter implements AttributeConverter<List<Map<String,Object>>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Map<String,Object>> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting List<Map> to JSON", e);
        }
    }

    @Override
    public List<Map<String,Object>> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<Map<String,Object>>>(){});
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to List<Map>", e);
        }
    }
}
