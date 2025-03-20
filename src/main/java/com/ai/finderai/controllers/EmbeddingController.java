package com.ai.finderai.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ai.finderai.dto.EmbeddingResponseDTO;
import com.ai.finderai.dto.GetEmbeddingRequestDTO;
import com.ai.finderai.services.EmbeddingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/embeddings")
@Tag(name = "Embeddings", description = "API for generating text embeddings")
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    public EmbeddingController(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @Operation(summary = "Generate a text embedding", description = "Generates an embedding vector for the given text using AI models such as OpenAI or DeepSeek.", requestBody = @RequestBody(description = "Input text to generate an embedding", required = true, content = @Content(schema = @Schema(implementation = GetEmbeddingRequestDTO.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Embedding successfully generated", content = @Content(schema = @Schema(implementation = EmbeddingResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<EmbeddingResponseDTO> getEmbedding(@Valid @RequestBody GetEmbeddingRequestDTO requestDTO) {
        EmbeddingResponseDTO responseDTO = embeddingService.generateEmbedding(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
