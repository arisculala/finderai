package com.ai.finderai.services;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Interface for AI provider clients responsible for generating text embeddings.
 */
@Schema(description = "Interface for AI provider clients responsible for generating text embeddings.")
public interface AIProviderClient {

    /**
     * Generates an embedding for the given text.
     *
     * @param text The input text to generate an embedding for.
     * @return An array of floating-point numbers representing the embedding.
     */
    float[] generateEmbedding(String text);
}
