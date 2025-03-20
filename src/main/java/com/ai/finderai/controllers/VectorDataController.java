package com.ai.finderai.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ai.finderai.dto.NewVectorDataRequestDTO;
import com.ai.finderai.dto.SearchVectorDataRequestDTO;
import com.ai.finderai.dto.VectorDataDTO;
import com.ai.finderai.services.VectorDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/vectors")
@Tag(name = "Vector Data", description = "API for storing and searching vector embeddings")
public class VectorDataController {

    private static final Logger logger = LoggerFactory.getLogger(VectorDataController.class);
    private final VectorDataService vectorDataService;

    public VectorDataController(VectorDataService vectorDataService) {
        this.vectorDataService = vectorDataService;
    }

    @Operation(summary = "Store a new vector embedding", description = "Saves a new vector embedding along with its metadata into the database.", requestBody = @RequestBody(description = "Vector data including text, embedding, and metadata", required = true, content = @Content(schema = @Schema(implementation = NewVectorDataRequestDTO.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Vector data successfully stored", content = @Content(schema = @Schema(implementation = VectorDataDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<VectorDataDTO> saveVector(
            @Valid @RequestBody NewVectorDataRequestDTO requestDTO) {
        logger.debug("Calling POST /api/v1/vectors endpoint");
        VectorDataDTO savedData = vectorDataService.saveVectorData(requestDTO);
        return ResponseEntity.ok(savedData);
    }

    @Operation(summary = "Search for closest vector embeddings", description = "Finds the closest vector embeddings based on the provided search request.", requestBody = @RequestBody(description = "Search parameters including text and the number of results to return", required = true, content = @Content(schema = @Schema(implementation = SearchVectorDataRequestDTO.class))), responses = {
            @ApiResponse(responseCode = "200", description = "List of closest vector embeddings", content = @Content(schema = @Schema(implementation = VectorDataDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<List<VectorDataDTO>> searchTextClosestRecords(
            @Valid @RequestBody SearchVectorDataRequestDTO searchRequestDTO) {
        logger.debug("Calling GET /api/v1/vectors/search endpoint");
        return ResponseEntity.ok(vectorDataService.searchTextClosestRecords(searchRequestDTO));
    }
}
