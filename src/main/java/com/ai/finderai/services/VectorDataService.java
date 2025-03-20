package com.ai.finderai.services;

import java.util.List;

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

@Service
public class VectorDataService {
    private static final Logger logger = LoggerFactory.getLogger(VectorDataService.class);

    private final DatabaseUtils databaseUtils;
    private final VectorDataRepository vectorDataRepository;
    private final VectorDataJDBCRepository vectorDataJDBCRepository;
    private final EmbeddingServiceFactory embeddingServiceFactory;

    public VectorDataService(DatabaseUtils databaseUtils, VectorDataRepository vectorDataRepository,
            VectorDataJDBCRepository vectorDataJDBCRepository, EmbeddingServiceFactory embeddingServiceFactory) {
        this.databaseUtils = databaseUtils;
        this.vectorDataRepository = vectorDataRepository;
        this.vectorDataJDBCRepository = vectorDataJDBCRepository;
        this.embeddingServiceFactory = embeddingServiceFactory;
    }

    public VectorDataDTO saveVectorData(NewVectorDataRequestDTO newVectorData) {
        logger.debug("Saving new vector data: {}", newVectorData);

        EmbeddingService embeddingService = embeddingServiceFactory.getEmbeddingService(newVectorData.getProvider());
        float[] rawEmbedding = embeddingService.generateEmbedding(newVectorData.getText());
        float[] processedEmbedding = databaseUtils.padEmbedding(rawEmbedding);

        VectorData vectorData = new VectorData(null, newVectorData.getProvider(),
                newVectorData.getModel(), newVectorData.getText(),
                processedEmbedding, newVectorData.getMetadata(), null);
        return new VectorDataDTO(vectorDataRepository.save(vectorData));
    }

    public List<VectorDataDTO> searchTextClosestRecords(SearchVectorDataRequestDTO searchRequestDTO) {
        logger.debug("Search text closest records: {}", searchRequestDTO);

        EmbeddingService embeddingService = embeddingServiceFactory.getEmbeddingService(searchRequestDTO.getProvider());
        float[] rawEmbedding = embeddingService.generateEmbedding(searchRequestDTO.getQuery());
        float[] paddedEmbedding = databaseUtils.padEmbedding(rawEmbedding);

        String vectorEmbeddingArray = databaseUtils.convertToPgVectorArray(paddedEmbedding);

        List<VectorDataDTO> results = vectorDataJDBCRepository.getClosestRecords(vectorEmbeddingArray,
                searchRequestDTO.getLimit());

        return results;
    }
}
