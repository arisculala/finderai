package com.ai.finderai.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DatabaseUtils {

    @Value("${embedding.max-dimension}")
    private int MAX_DIMENSION;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Convert JSONB String → Map<String, Object>
     *
     * @param jsonbString JSONB formatted string from PostgreSQL
     * @return Map representation of JSONB data
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> parseJsonbToMap(String jsonbString) {
        try {
            return jsonbString != null ? objectMapper.readValue(jsonbString, Map.class) : new HashMap<>();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSONB metadata", e);
        }
    }

    public float[] padEmbedding(float[] embedding) {
        float[] paddedEmbedding = new float[MAX_DIMENSION];
        System.arraycopy(embedding, 0, paddedEmbedding, 0, embedding.length);
        return paddedEmbedding;
    }

    public String convertToPgVectorArray(float[] embedding) {
        return Arrays.stream(toObjectArray(embedding)).map(Object::toString).collect(Collectors.joining(","));
    }

    public Float[] toObjectArray(float[] array) {
        Float[] floatObjects = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            floatObjects[i] = array[i];
        }
        return floatObjects;
    }
}
