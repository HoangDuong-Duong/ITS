package com.elcom.its.vds.convert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import javax.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hanh
 */
@Slf4j
public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> objInfo) {

        String objInfoJson = null;
        try {
            objInfoJson = objectMapper.writeValueAsString(objInfo);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error", e);
        }

        return objInfoJson;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String objInfoJSON) {

        Map<String, Object> objInfo = null;
        if (objInfoJSON != null) {
            try {
                objInfo = objectMapper.readValue(objInfoJSON, Map.class);
            } catch (final IOException e) {
                log.error("JSON reading error", e);
            }
        }

        return objInfo;
    }
}