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
    private final AIProviderClientFactory aProviderClientFactory;

    public VectorDataService(DatabaseUtils databaseUtils, VectorDataRepository vectorDataRepository,
            VectorDataJDBCRepository vectorDataJDBCRepository, AIProviderClientFactory embeddingServiceFactory) {
        this.databaseUtils = databaseUtils;
        this.vectorDataRepository = vectorDataRepository;
        this.vectorDataJDBCRepository = vectorDataJDBCRepository;
        this.aProviderClientFactory = embeddingServiceFactory;
    }

    public VectorDataDTO saveVectorData(NewVectorDataRequestDTO newVectorData) {
        logger.debug("Saving new vector data: {}", newVectorData);

        AIProviderClient aiProviderClient = aProviderClientFactory.getAIProviderClient(newVectorData.getProvider());
        float[] rawEmbedding = aiProviderClient.generateEmbedding(newVectorData.getText());
        float[] processedEmbedding = databaseUtils.padEmbedding(rawEmbedding);

        VectorData vectorData = new VectorData(null, newVectorData.getProvider(),
                newVectorData.getModel(), newVectorData.getText(),
                processedEmbedding, newVectorData.getMetadata(), null);
        return new VectorDataDTO(vectorDataRepository.save(vectorData));
    }

    public List<VectorDataDTO> searchTextClosestRecords(SearchVectorDataRequestDTO searchRequestDTO) {
        logger.debug("Search text closest records: {}", searchRequestDTO);

        AIProviderClient aiProviderClient = aProviderClientFactory.getAIProviderClient(searchRequestDTO.getProvider());
        float[] rawEmbedding = aiProviderClient.generateEmbedding(searchRequestDTO.getQuery());
        float[] paddedEmbedding = databaseUtils.padEmbedding(rawEmbedding);

        String vectorEmbeddingArray = databaseUtils.convertToPgVectorArray(paddedEmbedding);

        List<VectorDataDTO> results = vectorDataJDBCRepository.getClosestRecords(vectorEmbeddingArray,
                searchRequestDTO.getLimit());

        return results;
    }
}
