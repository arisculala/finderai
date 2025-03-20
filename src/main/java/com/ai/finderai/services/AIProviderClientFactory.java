package com.ai.finderai.services;

import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Factory class for selecting the appropriate AI provider client dynamically.
 */
@Service
@Schema(description = "Factory for managing AI provider clients dynamically based on configuration.")
public class AIProviderClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(AIProviderClientFactory.class);

    @Value("${aiprovider.default-provider}")
    private String defaultProvider;

    private final Map<String, AIProviderClient> aiProviderClients;

    @Autowired
    public AIProviderClientFactory(Map<String, AIProviderClient> aiProviderClients) {
        this.aiProviderClients = aiProviderClients;
    }

    /**
     * Retrieves an AI provider client based on the given provider name.
     *
     * @param provider The name of the AI provider (e.g., "openai", "deepseek").
     * @return The corresponding AIProviderClient implementation.
     */
    public AIProviderClient getAIProviderClient(String provider) {
        AIProviderClient client = aiProviderClients.getOrDefault(provider, aiProviderClients.get(defaultProvider));

        if (client == null) {
            logger.error("No AI provider client found for provider: {} and default provider: {}", provider,
                    defaultProvider);
            throw new IllegalArgumentException("Invalid AI provider: " + provider);
        }

        logger.debug("Selected AI provider client: {}", provider);
        return client;
    }
}
