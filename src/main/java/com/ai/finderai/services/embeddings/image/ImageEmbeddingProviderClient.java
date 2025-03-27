package com.ai.finderai.services.embeddings.image;

/**
 * Interface for image embedding providers.
 */
public interface ImageEmbeddingProviderClient {

    /**
     * Generates an image embedding vector from an image URL.
     * 
     * @param imageUrl The URL of the image to process.
     * @return A float array representing the embedding vector.
     */
    float[] generateEmbeddingFromImage(String imageUrl);
}
