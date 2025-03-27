package com.ai.finderai.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.finderai.dto.NewVectorDataRequestDTO;
import com.ai.finderai.dto.SearchVectorDataRequestDTO;
import com.ai.finderai.dto.VectorDataDTO;
import com.ai.finderai.services.VectorDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/vectors")
@Tag(name = "Vector API", description = "API for storing and searching vector embeddings")
public class VectorDataController {

    private static final Logger logger = LoggerFactory.getLogger(VectorDataController.class);
    private final VectorDataService vectorDataService;

    public VectorDataController(VectorDataService vectorDataService) {
        this.vectorDataService = vectorDataService;
    }

    @Operation(summary = "Store a new text-based vector embedding", description = "Saves a new text embedding into the database.")
    @PostMapping("/text")
    public ResponseEntity<VectorDataDTO> saveTextEmbedding(
            @Valid @RequestBody NewVectorDataRequestDTO requestDTO) {
        logger.debug("Calling POST /api/v1/vectors/text endpoint");
        VectorDataDTO savedData = vectorDataService.saveTextEmbedding(requestDTO);
        return ResponseEntity.ok(savedData);
    }

    @Operation(summary = "Store a new image-based vector embedding", description = "Saves a new image embedding into the database.")
    @PostMapping("/image")
    public ResponseEntity<VectorDataDTO> saveImageEmbedding(
            @Valid @RequestBody NewVectorDataRequestDTO requestDTO) {
        logger.debug("Calling POST /api/v1/vectors/image endpoint");
        VectorDataDTO savedData = vectorDataService.saveImageEmbedding(requestDTO);
        return ResponseEntity.ok(savedData);
    }

    @Operation(summary = "Search for closest vector embeddings", description = "Finds the closest vector embeddings based on the input query.")
    @PostMapping("/search")
    public ResponseEntity<List<VectorDataDTO>> searchClosestRecords(
            @Valid @RequestBody SearchVectorDataRequestDTO searchRequestDTO) {
        logger.debug("Calling POST /api/v1/vectors/search endpoint");
        return ResponseEntity.ok(vectorDataService.searchClosestRecords(searchRequestDTO));
    }
}
