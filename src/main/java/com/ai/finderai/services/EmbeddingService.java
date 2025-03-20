package com.ai.finderai.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ai.finderai.dto.EmbeddingResponseDTO;
import com.ai.finderai.dto.GetEmbeddingRequestDTO;
import com.ai.finderai.utils.DatabaseUtils;

@Service
public class EmbeddingService {
    private static final Logger logger = LoggerFactory.getLogger(EmbeddingService.class);

    private final DatabaseUtils databaseUtils;
    private final AIProviderClientFactory aProviderClientFactory;

    public EmbeddingService(AIProviderClientFactory embeddingServiceFactory, DatabaseUtils databaseUtils) {
        this.aProviderClientFactory = embeddingServiceFactory;
        this.databaseUtils = databaseUtils;
    }

    public EmbeddingResponseDTO generateEmbedding(GetEmbeddingRequestDTO requestDTO) {
        logger.debug("Generate embedding: {}", requestDTO);

        AIProviderClient aiProviderClient = aProviderClientFactory.getAIProviderClient(requestDTO.getProvider());
        float[] rawEmbedding = aiProviderClient.generateEmbedding(requestDTO.getText());
        return new EmbeddingResponseDTO(requestDTO.getProvider(), requestDTO.getModel(), rawEmbedding,
                databaseUtils.MAX_DIMENSION);
    }
}
