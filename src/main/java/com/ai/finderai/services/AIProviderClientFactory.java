package com.ai.finderai.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AIProviderClientFactory {
    private static final Logger logger = LoggerFactory.getLogger(AIProviderClientFactory.class);

    @Value("${aiprovider.default-provider}")
    private String defaultProvider;

    private final Map<String, AIProviderClient> aiProviderClients;

    @Autowired
    public AIProviderClientFactory(Map<String, AIProviderClient> aiProviderClients) {
        this.aiProviderClients = aiProviderClients;
    }

    public AIProviderClient getAIProviderClient(String provider) {
        logger.debug("Getting AI provider client: {}", provider);
        return aiProviderClients.getOrDefault(provider, aiProviderClients.get(defaultProvider));
    }
}
