package com.ai.finderai.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.finderai.dto.embedding.CsvEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.DatabaseEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.FileEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.ImageEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.TextEmbeddingRequestDTO;
import com.ai.finderai.services.EmbeddingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/embeddings")
@Tag(name = "Embedding API", description = "API for generating embeddings")
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    public EmbeddingController(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @Operation(summary = "Generate text embedding", description = "Returns the embedding vector for a given text.")
    @PostMapping("/text")
    public ResponseEntity<float[]> generateTextEmbedding(@Valid @RequestBody TextEmbeddingRequestDTO request) {
        return ResponseEntity.ok(embeddingService.generateTextEmbedding(request));
    }

    @Operation(summary = "Generate image embedding", description = "Returns the embedding vector for a given image.")
    @PostMapping("/image")
    public ResponseEntity<float[]> generateImageEmbedding(@Valid @RequestBody ImageEmbeddingRequestDTO request) {
        return ResponseEntity.ok(embeddingService.generateImageEmbedding(request));
    }

    @Operation(summary = "Generate CSV embedding", description = "Processes a CSVfile andreturns embeddings.")
    @PostMapping("/csv")
    public ResponseEntity<float[]> generateCsvEmbedding(@Valid @RequestBody CsvEmbeddingRequestDTO request) {
        return ResponseEntity.ok(embeddingService.generateCsvEmbedding(request));
    }

    @Operation(summary = "Generate database embedding", description = "Processesa databasetable andreturns embeddings.")
    @PostMapping("/database")
    public ResponseEntity<float[]> generateDatabaseEmbedding(@Valid @RequestBody DatabaseEmbeddingRequestDTO request) {
        return ResponseEntity.ok(embeddingService.generateDatabaseEmbedding(request));
    }

    @Operation(summary = "Generate file embedding", description = "Processes a file (PDF, DOCX, etc.) and returns embeddings.")
    @PostMapping("/file")
    public ResponseEntity<float[]> generateFileEmbedding(@Valid @RequestBody FileEmbeddingRequestDTO request) {
        return ResponseEntity.ok(embeddingService.generateFileEmbedding(request));
    }
}
