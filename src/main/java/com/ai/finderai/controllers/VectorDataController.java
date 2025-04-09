package com.ai.finderai.controllers;

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

    @Operation(summary = "Store a new text embedding", description = "Saves a new text embedding into the database.")
    @PostMapping
    public ResponseEntity<VectorDataDTO> saveTextEmbedding(@Valid @RequestBody NewVectorDataRequestDTO requestDTO) {
        return ResponseEntity.ok(vectorDataService.saveEmbedding(requestDTO));
    }

    @Operation(summary = "Search for similar vectors", description = "Finds the closest matching vector records based on input query.")
    @PostMapping("/search")
    public ResponseEntity<?> searchVectorData(@Valid @RequestBody SearchVectorDataRequestDTO requestDTO) {
        return ResponseEntity.ok(vectorDataService.searchClosestRecords(requestDTO));
    }
}
