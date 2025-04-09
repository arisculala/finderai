package com.ai.finderai.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ai.finderai.dto.NewVectorDataRequestDTO;
import com.ai.finderai.dto.SearchVectorDataRequestDTO;
import com.ai.finderai.dto.VectorDataDTO;
import com.ai.finderai.enums.EmbeddingType;
import com.ai.finderai.models.VectorData;
import com.ai.finderai.repositories.VectorDataJDBCRepository;
import com.ai.finderai.repositories.VectorDataRepository;
import com.ai.finderai.services.embeddings.image.ImageEmbeddingProviderClient;
import com.ai.finderai.services.embeddings.image.ImageEmbeddingProviderClientFactory;
import com.ai.finderai.services.embeddings.text.TextEmbeddingProviderClient;
import com.ai.finderai.services.embeddings.text.TextEmbeddingProviderClientFactory;
import com.ai.finderai.utils.DatabaseUtils;
import com.generic.exceptions.InternalServerException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Service for managing vector data, including saving new vectors and searching
 * for closest matches.
 */
@Service
@Tag(name = "Vector Data Service", description = "Service for handling vector data storage and retrieval.")
public class VectorDataService {

    private static final Logger logger = LoggerFactory.getLogger(VectorDataService.class);

    private final DatabaseUtils databaseUtils;
    private final VectorDataRepository vectorDataRepository;
    private final VectorDataJDBCRepository vectorDataJDBCRepository;
    private final TextEmbeddingProviderClientFactory textEmbeddingProviderFactory;
    private final ImageEmbeddingProviderClientFactory imageEmbeddingProviderFactory;

    public VectorDataService(
            DatabaseUtils databaseUtils,
            VectorDataRepository vectorDataRepository,
            VectorDataJDBCRepository vectorDataJDBCRepository,
            TextEmbeddingProviderClientFactory textEmbeddingProviderFactory,
            ImageEmbeddingProviderClientFactory imageEmbeddingProviderFactory) {
        this.databaseUtils = databaseUtils;
        this.vectorDataRepository = vectorDataRepository;
        this.vectorDataJDBCRepository = vectorDataJDBCRepository;
        this.textEmbeddingProviderFactory = textEmbeddingProviderFactory;
        this.imageEmbeddingProviderFactory = imageEmbeddingProviderFactory;
    }

    public VectorDataDTO saveEmbedding(NewVectorDataRequestDTO newVectorData) {
        logger.debug("Saving new embedding: {}", newVectorData);

        float[] paddedEmbedding = databaseUtils.padEmbedding(newVectorData.getEmbedding());

        VectorData vectorData = new VectorData();
        vectorData.setEmbeddingType(newVectorData.getEmbeddingType());
        vectorData.setProvider(newVectorData.getProvider());
        vectorData.setModel(newVectorData.getModel());
        vectorData.setSource(newVectorData.getSource());
        vectorData.setEmbedding(paddedEmbedding);
        vectorData.setMetadata(newVectorData.getMetadata());

        return new VectorDataDTO(vectorDataRepository.save(vectorData));
    }

    @Operation(summary = "Search closest vector records", description = "Finds the closest matching vector records based on input query.")
    public List<VectorDataDTO> searchClosestRecords(SearchVectorDataRequestDTO searchRequestDTO) {
        logger.info("Starting search for: {}", searchRequestDTO);

        // Get the types directly from the request (no need to convert from Set<String>
        // to Set<EmbeddingType>)
        Set<EmbeddingType> types = searchRequestDTO.getTypes();
        List<VectorDataDTO> results = new ArrayList<>();

        try {
            // Check for "TEXT" type
            if (types.contains(EmbeddingType.TEXT)) {
                TextEmbeddingProviderClient textClient = textEmbeddingProviderFactory
                        .getAIProviderClient(searchRequestDTO.getProvider());
                float[] textEmbedding = textClient.generateEmbeddingFromText(searchRequestDTO.getQuery());
                results.addAll(searchByEmbedding(textEmbedding, searchRequestDTO.getLimit()));
            }

            // Check for "IMAGE" type
            if (types.contains(EmbeddingType.IMAGE)) {
                ImageEmbeddingProviderClient imageClient = imageEmbeddingProviderFactory
                        .getAIProviderClient(searchRequestDTO.getProvider());
                float[] imageEmbedding = imageClient.generateEmbeddingFromImage(searchRequestDTO.getQuery());
                results.addAll(searchByEmbedding(imageEmbedding, searchRequestDTO.getLimit()));
            }

            // Check for other types if needed (e.g., file, audio, etc.)
            // You can add further checks here based on other types in the Enum

            if (results.isEmpty()) {
                logger.warn("No results found for types: {}", types);
            }

        } catch (Exception e) {
            logger.error("Error during search operation", e);
            throw new InternalServerException("Search operation failed", e);
        }

        return results;
    }

    private List<VectorDataDTO> searchByEmbedding(float[] embedding, int limit) {
        logger.debug("Searching database with embedding vector...");

        float[] paddedEmbedding = databaseUtils.padEmbedding(embedding);
        String vectorEmbeddingArray = databaseUtils.convertToPgVectorArray(paddedEmbedding);

        List<VectorDataDTO> searchResults = vectorDataJDBCRepository.getClosestRecords(vectorEmbeddingArray, limit);

        logger.info("Found {} matching records", searchResults.size());
        return searchResults;
    }
}
