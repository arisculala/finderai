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
import com.ai.finderai.services.kafka.EmbeddingKafkaProducer;

@RestController
@RequestMapping("/api/v1/bulk")
public class BulkEmbeddingController {
    private final EmbeddingKafkaProducer embeddingKafkaProducer;

    public BulkEmbeddingController(EmbeddingKafkaProducer embeddingKafkaProducer) {
        this.embeddingKafkaProducer = embeddingKafkaProducer;
    }

    @PostMapping("/text")
    public ResponseEntity<String> processTextEmbedding(@RequestBody TextEmbeddingRequestDTO request) {
        embeddingKafkaProducer.sendTextEmbeddingRequest(request);
        return ResponseEntity.ok("Text embedding request sent to Kafka.");
    }

    @PostMapping("/image")
    public ResponseEntity<String> processImageEmbedding(@RequestBody ImageEmbeddingRequestDTO request) {
        embeddingKafkaProducer.sendImageEmbeddingRequest(request);
        return ResponseEntity.ok("Image embedding request sent to Kafka.");
    }

    @PostMapping("/csv")
    public ResponseEntity<String> processCsvEmbedding(@RequestBody CsvEmbeddingRequestDTO request) {
        embeddingKafkaProducer.sendCsvEmbeddingRequest(request);
        return ResponseEntity.ok("CSV embedding request sent to Kafka.");
    }

    @PostMapping("/database")
    public ResponseEntity<String> processDatabaseEmbedding(@RequestBody DatabaseEmbeddingRequestDTO request) {
        embeddingKafkaProducer.sendDatabaseEmbeddingRequest(request);
        return ResponseEntity.ok("Database embedding request sent to Kafka.");
    }

    @PostMapping("/file")
    public ResponseEntity<String> processFileEmbedding(@RequestBody FileEmbeddingRequestDTO request) {
        embeddingKafkaProducer.sendFileEmbeddingRequest(request);
        return ResponseEntity.ok("File embedding request sent to Kafka.");
    }
}
