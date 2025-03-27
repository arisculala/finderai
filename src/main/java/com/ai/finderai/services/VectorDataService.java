package com.ai.finderai.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ai.finderai.dto.NewVectorDataRequestDTO;
import com.ai.finderai.dto.SearchVectorDataRequestDTO;
import com.ai.finderai.dto.VectorDataDTO;
import com.ai.finderai.models.ImageVectorData;
import com.ai.finderai.models.TextVectorData;
import com.ai.finderai.models.VectorData;
import com.ai.finderai.repositories.VectorDataJDBCRepository;
import com.ai.finderai.repositories.VectorDataRepository;
import com.ai.finderai.services.embeddings.image.ImageEmbeddingProviderClient;
import com.ai.finderai.services.embeddings.image.ImageEmbeddingProviderClientFactory;
import com.ai.finderai.services.embeddings.text.TextEmbeddingProviderClient;
import com.ai.finderai.services.embeddings.text.TextEmbeddingProviderClientFactory;
import com.ai.finderai.utils.DatabaseUtils;

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

    @Operation(summary = "Save text vector data", description = "Generates and saves vector embedding from input text.")
    public VectorDataDTO saveTextEmbedding(NewVectorDataRequestDTO newVectorData) {
        logger.debug("Saving new text embedding: {}", newVectorData);

        TextEmbeddingProviderClient aiProviderClient = textEmbeddingProviderFactory
                .getAIProviderClient(newVectorData.getProvider());
        float[] rawEmbedding = aiProviderClient.generateEmbeddingFromText(newVectorData.getText());
        float[] processedEmbedding = databaseUtils.padEmbedding(rawEmbedding);

        TextVectorData textVectorData = new TextVectorData();
        textVectorData.setProvider(newVectorData.getProvider());
        textVectorData.setModel(newVectorData.getModel());
        textVectorData.setText(newVectorData.getText());
        textVectorData.setEmbedding(processedEmbedding);
        textVectorData.setMetadata(newVectorData.getMetadata());

        return new VectorDataDTO(vectorDataRepository.save(textVectorData));
    }

    @Operation(summary = "Save image vector data", description = "Generates and saves vector embedding from an image URL.")
    public VectorDataDTO saveImageEmbedding(NewVectorDataRequestDTO newVectorData) {
        logger.debug("Saving new image embedding: {}", newVectorData);

        ImageEmbeddingProviderClient aiProviderClient = imageEmbeddingProviderFactory
                .getAIProviderClient(newVectorData.getProvider());
        float[] rawEmbedding = aiProviderClient.generateEmbeddingFromImage(newVectorData.getText()); // Assuming `text`
                                                                                                     // holds an image
                                                                                                     // URL
        float[] processedEmbedding = databaseUtils.padEmbedding(rawEmbedding);

        ImageVectorData imageVectorData = new ImageVectorData();
        imageVectorData.setProvider(newVectorData.getProvider());
        imageVectorData.setModel(newVectorData.getModel());
        imageVectorData.setImageUrl(newVectorData.getText());
        imageVectorData.setEmbedding(processedEmbedding);
        imageVectorData.setMetadata(newVectorData.getMetadata());

        return new VectorDataDTO(vectorDataRepository.save(imageVectorData));
    }

    @Operation(summary = "Search closest vector records", description = "Finds the closest matching vector records based on input query.")
    public List<VectorDataDTO> searchClosestRecords(SearchVectorDataRequestDTO searchRequestDTO) {
        logger.debug("Searching closest vector records: {}", searchRequestDTO);

        Set<String> types = new HashSet<>(searchRequestDTO.getTypes()); // text, image, etc.
        List<VectorDataDTO> results = new ArrayList<>();

        if (types.contains("text")) {
            TextEmbeddingProviderClient textClient = textEmbeddingProviderFactory
                    .getAIProviderClient(searchRequestDTO.getProvider());
            float[] textEmbedding = textClient.generateEmbeddingFromText(searchRequestDTO.getQuery());
            results.addAll(searchByEmbedding(textEmbedding, searchRequestDTO.getLimit()));
        }

        if (types.contains("image")) {
            ImageEmbeddingProviderClient imageClient = imageEmbeddingProviderFactory
                    .getAIProviderClient(searchRequestDTO.getProvider());
            float[] imageEmbedding = imageClient.generateEmbeddingFromImage(searchRequestDTO.getQuery());
            results.addAll(searchByEmbedding(imageEmbedding, searchRequestDTO.getLimit()));
        }

        if (results.isEmpty()) {
            throw new IllegalArgumentException("No valid embedding type provided: " + types);
        }

        return results;
    }

    private List<VectorDataDTO> searchByEmbedding(float[] embedding, int limit) {
        float[] paddedEmbedding = databaseUtils.padEmbedding(embedding);
        String vectorEmbeddingArray = databaseUtils.convertToPgVectorArray(paddedEmbedding);
        return vectorDataJDBCRepository.getClosestRecords(vectorEmbeddingArray, limit);
    }
}
