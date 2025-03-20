package com.ai.finderai.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class EmbeddingServiceFactory {
    private static final Logger logger = LoggerFactory.getLogger(EmbeddingServiceFactory.class);

    @Value("${embedding.default-provider}")
    private String defaultProvider;

    private final Map<String, EmbeddingService> embeddingServices;

    @Autowired
    public EmbeddingServiceFactory(Map<String, EmbeddingService> embeddingServices) {
        this.embeddingServices = embeddingServices;
    }

    public EmbeddingService getEmbeddingService(String provider) {
        logger.debug("Getting embedding service for provider: {}", provider);
        String embeddingService = provider + "-embedding-service";
        String defaultProviderService = defaultProvider + "-embedding-service";
        return embeddingServices.getOrDefault(embeddingService, embeddingServices.get(defaultProviderService));
    }
}
