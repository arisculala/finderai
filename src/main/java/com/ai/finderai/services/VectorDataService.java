package com.ai.finderai.services;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ai.finderai.dto.NewVectorDataRequestDTO;
import com.ai.finderai.dto.SearchVectorDataRequestDTO;
import com.ai.finderai.dto.VectorDataDTO;
import com.ai.finderai.models.VectorData;
import com.ai.finderai.repositories.VectorDataJDBCRepository;
import com.ai.finderai.repositories.VectorDataRepository;
import com.ai.finderai.utils.DatabaseUtils;
import java.util.List;

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
    private final AIProviderClientFactory aiProviderClientFactory;

    /**
     * Constructor for VectorDataService.
     *
     * @param databaseUtils            Utility class for database operations.
     * @param vectorDataRepository     JPA repository for vector data persistence.
     * @param vectorDataJDBCRepository JDBC repository for performing vector
     *                                 similarity searches.
     * @param aiProviderClientFactory  Factory for selecting the appropriate AI
     *                                 provider client.
     */
    public VectorDataService(DatabaseUtils databaseUtils,
            VectorDataRepository vectorDataRepository,
            VectorDataJDBCRepository vectorDataJDBCRepository,
            AIProviderClientFactory aiProviderClientFactory) {
        this.databaseUtils = databaseUtils;
        this.vectorDataRepository = vectorDataRepository;
        this.vectorDataJDBCRepository = vectorDataJDBCRepository;
        this.aiProviderClientFactory = aiProviderClientFactory;
    }

    /**
     * Saves a new vector embedding generated from the given text.
     *
     * @param newVectorData Request DTO containing text, provider, and metadata.
     * @return VectorDataDTO containing the saved vector data.
     */
    @Operation(summary = "Save vector data", description = "Generates and saves vector embedding from input text.")
    public VectorDataDTO saveVectorData(NewVectorDataRequestDTO newVectorData) {
        logger.debug("Saving new vector data: {}", newVectorData);

        AIProviderClient aiProviderClient = aiProviderClientFactory.getAIProviderClient(newVectorData.getProvider());
        float[] rawEmbedding = aiProviderClient.generateEmbedding(newVectorData.getText());
        float[] processedEmbedding = databaseUtils.padEmbedding(rawEmbedding);

        VectorData vectorData = new VectorData(
                null,
                newVectorData.getProvider(),
                newVectorData.getModel(),
                newVectorData.getText(),
                processedEmbedding,
                newVectorData.getMetadata(),
                null);

        return new VectorDataDTO(vectorDataRepository.save(vectorData));
    }

    /**
     * Searches for the closest matching vector records based on a query text.
     *
     * @param searchRequestDTO Request DTO containing the search query, provider,
     *                         and result limit.
     * @return List of VectorDataDTO containing the closest matching records.
     */
    @Operation(summary = "Search closest vector records", description = "Finds the closest matching vector records based on the input text query.")
    public List<VectorDataDTO> searchTextClosestRecords(SearchVectorDataRequestDTO searchRequestDTO) {
        logger.debug("Searching closest vector records: {}", searchRequestDTO);

        AIProviderClient aiProviderClient = aiProviderClientFactory.getAIProviderClient(searchRequestDTO.getProvider());
        float[] rawEmbedding = aiProviderClient.generateEmbedding(searchRequestDTO.getQuery());
        float[] paddedEmbedding = databaseUtils.padEmbedding(rawEmbedding);

        String vectorEmbeddingArray = databaseUtils.convertToPgVectorArray(paddedEmbedding);

        return vectorDataJDBCRepository.getClosestRecords(vectorEmbeddingArray, searchRequestDTO.getLimit());
    }
}
