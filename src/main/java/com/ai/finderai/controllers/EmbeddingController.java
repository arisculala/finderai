package com.ai.finderai.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.finderai.services.EmbeddingService;
import com.ai.finderai.services.EmbeddingServiceFactory;

@RestController
@RequestMapping("/api/v1/embeddings")
public class EmbeddingController {
    private final EmbeddingServiceFactory embeddingServiceFactory;

    public EmbeddingController(EmbeddingServiceFactory embeddingServiceFactory) {
        this.embeddingServiceFactory = embeddingServiceFactory;
    }

    @GetMapping("/{provider}")
    public ResponseEntity<float[]> getEmbedding(
            @PathVariable String provider,
            @RequestParam String text) {
        EmbeddingService embeddingService = embeddingServiceFactory.getEmbeddingService(provider);
        float[] embedding = embeddingService.generateEmbedding(text);
        return ResponseEntity.ok(embedding);
    }
}
