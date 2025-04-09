package com.ai.finderai.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ai.finderai.dto.NewVectorDataRequestDTO;
import com.ai.finderai.dto.embedding.CsvEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.DatabaseEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.FileEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.ImageEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.TextEmbeddingRequestDTO;
import com.ai.finderai.enums.EmbeddingType;
import com.ai.finderai.services.embeddings.text.TextEmbeddingProviderClient;
import com.ai.finderai.services.embeddings.text.TextEmbeddingProviderClientFactory;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Service responsible for generating text embeddings using AI provider clients.
 */
@Service
@Tag(name = "Embedding Service", description = "Service for generating text embeddings using different AI providers.")
public class EmbeddingService {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddingService.class);

    @Value("${application.embeddings.max-texts-limit:10}")
    private int maxTextsLimit;

    private final VectorDataService vectorDataService;
    private final TextEmbeddingProviderClientFactory textEmbeddingProviderClientFactory;

    public EmbeddingService(VectorDataService vectorDataService,
            TextEmbeddingProviderClientFactory textEmbeddingProviderClientFactory) {
        this.vectorDataService = vectorDataService;
        this.textEmbeddingProviderClientFactory = textEmbeddingProviderClientFactory;
    }

    public float[] generateTextEmbedding(TextEmbeddingRequestDTO textEmbeddingRequestDTO) {
        logger.debug("Generating text embedding: {}", textEmbeddingRequestDTO);

        TextEmbeddingProviderClient textEmbeddingProviderClient = textEmbeddingProviderClientFactory
                .getAIProviderClient(textEmbeddingRequestDTO.getProvider());

        float[] embedding = textEmbeddingProviderClient.generateEmbeddingFromText(textEmbeddingRequestDTO.getText());

        // Save embedding to database if 'save' flag is true
        if (textEmbeddingRequestDTO.isSave()) {
            saveEmbeddingToDatabase(
                    EmbeddingType.TEXT,
                    textEmbeddingRequestDTO.getProvider(),
                    textEmbeddingRequestDTO.getModel(),
                    textEmbeddingRequestDTO.getText(),
                    textEmbeddingRequestDTO.getMetadata(),
                    embedding);
        }

        return embedding;
    }

    public float[] generateImageEmbedding(ImageEmbeddingRequestDTO imageEmbeddingRequestDTO) {
        logger.debug("Generating image embedding: {}", imageEmbeddingRequestDTO);
        // Call AI provider to generate image embeddings
        return new float[] { 0.4f, 0.5f, 0.6f };
    }

    public float[] generateDatabaseEmbedding(DatabaseEmbeddingRequestDTO databaseEmbeddingRequestDTO) {
        logger.debug("Generating database embedding: {}", databaseEmbeddingRequestDTO);
        // Query DB, process records, generate embeddings
        return new float[] { 1.0f, 1.1f, 1.2f };
    }

    public float[] generateFileEmbedding(FileEmbeddingRequestDTO fileEmbeddingRequestDTO) {
        logger.debug("Generating file embedding: {}", fileEmbeddingRequestDTO);
        // Extract text from file, generate embeddings
        return new float[] { 1.3f, 1.4f, 1.5f };
    }

    public float[] generateCsvEmbedding(CsvEmbeddingRequestDTO csvEmbeddingRequestDTO) {
        logger.debug("Generating csv embedding: {}", csvEmbeddingRequestDTO);
        // Extract text from file, generate embeddings
        return new float[] { 1.3f, 1.4f, 1.5f };
    }

    public void saveEmbeddingToDatabase(EmbeddingType embeddingType, String provider, String model, String source,
            Map<String, Object> metadata, float[] embeddings) {
        logger.debug("Saving embedding to database...");

        vectorDataService.saveEmbedding(new NewVectorDataRequestDTO(
                embeddingType,
                provider,
                model,
                source,
                metadata,
                embeddings));
    }
}
