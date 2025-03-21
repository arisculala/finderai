package com.ai.finderai.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for handling database-related operations
 */
@Component
public class DatabaseUtils {

    /**
     * The maximum dimension of the embedding vector.
     * This value is injected from the application properties.
     */
    @Value("${aiprovider.max-dimension}")
    public int MAX_DIMENSION;

    // ObjectMapper for JSON processing
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converts a JSONB-formatted string from PostgreSQL into a Map<String, Object>.
     *
     * @param jsonbString JSONB string retrieved from the database
     * @return A map representing the JSONB data, or an empty map if parsing fails
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> parseJsonbToMap(String jsonbString) {
        try {
            return jsonbString != null ? objectMapper.readValue(jsonbString, Map.class) : new HashMap<>();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSONB metadata", e);
        }
    }

    /**
     * Pads the given embedding vector with zeros to match the maximum dimension.
     * If the input embedding is shorter than MAX_DIMENSION, it fills the remaining
     * positions with zeros.
     *
     * @param embedding The original embedding array
     * @return A new array with length MAX_DIMENSION
     */
    public float[] padEmbedding(float[] embedding) {
        float[] paddedEmbedding = new float[MAX_DIMENSION];
        System.arraycopy(embedding, 0, paddedEmbedding, 0, embedding.length);
        return paddedEmbedding;
    }

    /**
     * Converts a float array into a PostgreSQL vector-compatible string format.
     * PostgreSQL stores vectors in the format '{x1, x2, x3, ...}'.
     *
     * @param embedding The float array representing the vector
     * @return A string formatted as "x1,x2,x3,..." (without curly braces)
     */
    public String convertToPgVectorArray(float[] embedding) {
        return Arrays.stream(toObjectArray(embedding))
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    /**
     * Converts a primitive float array to a Float object array.
     * This is required for stream processing since primitive arrays are not
     * directly compatible with Streams.
     *
     * @param array The primitive float array
     * @return A Float object array
     */
    public Float[] toObjectArray(float[] array) {
        Float[] floatObjects = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            floatObjects[i] = array[i];
        }
        return floatObjects;
    }
}
