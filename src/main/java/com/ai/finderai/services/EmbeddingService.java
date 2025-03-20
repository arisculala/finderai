package com.ai.finderai.services;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ai.finderai.dto.EmbeddingResponseDTO;
import com.ai.finderai.dto.GetEmbeddingRequestDTO;
import com.ai.finderai.utils.DatabaseUtils;

/**
 * Service responsible for generating text embeddings using AI provider clients.
 */
@Service
@Tag(name = "Embedding Service", description = "Service for generating text embeddings using different AI providers.")
public class EmbeddingService {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddingService.class);

    private final DatabaseUtils databaseUtils;
    private final AIProviderClientFactory aiProviderClientFactory;

    /**
     * Constructor for EmbeddingService.
     *
     * @param aiProviderClientFactory Factory for selecting the appropriate AI
     *                                provider client.
     * @param databaseUtils           Utility class for handling database-related
     *                                operations.
     */
    public EmbeddingService(AIProviderClientFactory aiProviderClientFactory, DatabaseUtils databaseUtils) {
        this.aiProviderClientFactory = aiProviderClientFactory;
        this.databaseUtils = databaseUtils;
    }

    /**
     * Generates an embedding for the provided text using the specified AI provider.
     *
     * @param requestDTO Request DTO containing provider name, model, and input
     *                   text.
     * @return EmbeddingResponseDTO containing the generated embedding and metadata.
     */
    @Operation(summary = "Generate text embedding", description = "Generates an embedding using the specified AI provider.")
    public EmbeddingResponseDTO generateEmbedding(GetEmbeddingRequestDTO requestDTO) {
        logger.debug("Generating embedding: {}", requestDTO);
        logger.debug("Generating embedding getProvider: {}", requestDTO.getProvider());
        logger.debug("Generating embedding getModel: {}", requestDTO.getModel());
        logger.debug("Generating embedding getText: {}", requestDTO.getText());

        AIProviderClient aiProviderClient = aiProviderClientFactory.getAIProviderClient(requestDTO.getProvider());
        float[] rawEmbedding = aiProviderClient.generateEmbedding(requestDTO.getText());

        return new EmbeddingResponseDTO(
                requestDTO.getProvider(),
                requestDTO.getModel(),
                rawEmbedding,
                databaseUtils.MAX_DIMENSION);
    }
}
