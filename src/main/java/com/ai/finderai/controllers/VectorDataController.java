package com.ai.finderai.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.finderai.dto.NewVectorDataRequestDTO;
import com.ai.finderai.dto.SearchVectorDataRequestDTO;
import com.ai.finderai.dto.VectorDataDTO;
import com.ai.finderai.services.VectorDataService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/vectors")
public class VectorDataController {
    private static final Logger logger = LoggerFactory.getLogger(VectorDataController.class);

    private final VectorDataService vectorDataService;

    public VectorDataController(VectorDataService vectorDataService) {
        this.vectorDataService = vectorDataService;
    }

    @PostMapping
    public ResponseEntity<VectorDataDTO> saveVector(
            @Valid @RequestBody NewVectorDataRequestDTO requestDTO) {
        logger.debug("Calling POST /api/v1/vectors endpoint");
        VectorDataDTO savedData = vectorDataService.saveVectorData(requestDTO);
        return ResponseEntity.ok(savedData);
    }

    @GetMapping("/search")
    public ResponseEntity<List<VectorDataDTO>> searchTextClosestRecords(
            @Valid @RequestBody SearchVectorDataRequestDTO searchRequestDTO) {
        logger.debug("Calling GET /api/v1/search endpoint");
        return ResponseEntity.ok(vectorDataService.searchTextClosestRecords(searchRequestDTO));
    }
}
