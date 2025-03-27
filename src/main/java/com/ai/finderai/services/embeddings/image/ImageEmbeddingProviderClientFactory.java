package com.ai.finderai.services.embeddings.image;

import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Factory for selecting the appropriate image embedding provider client.
 */
@Component
public class ImageEmbeddingProviderClientFactory {

    private final Map<String, ImageEmbeddingProviderClient> providers;

    public ImageEmbeddingProviderClientFactory(CLIPImageEmbeddingProvider clipProvider) {
        this.providers = Map.of(
                "clip", clipProvider // Add more providers here as needed
        );
    }

    /**
     * Retrieves the appropriate image embedding provider.
     *
     * @param provider The provider name (e.g., "clip").
     * @return The corresponding ImageEmbeddingProviderClient.
     */
    public ImageEmbeddingProviderClient getAIProviderClient(String provider) {
        if (!providers.containsKey(provider)) {
            throw new IllegalArgumentException("Unsupported image embedding provider: " + provider);
        }
        return providers.get(provider);
    }
}
