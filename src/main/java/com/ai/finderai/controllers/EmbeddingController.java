package com.ai.finderai.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.finderai.dto.EmbeddingResponseDTO;
import com.ai.finderai.dto.GetEmbeddingRequestDTO;
import com.ai.finderai.services.EmbeddingService;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Generate a text embedding", description = "Generates an embedding vector for the given text using AI models such as OpenAI or DeepSeek.")
    @GetMapping
    public ResponseEntity<EmbeddingResponseDTO> getEmbedding(@Valid @RequestBody GetEmbeddingRequestDTO requestDTO) {
        EmbeddingResponseDTO responseDTO = embeddingService.generateEmbedding(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
