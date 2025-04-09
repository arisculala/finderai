package com.ai.finderai.dto;

import com.ai.finderai.enums.EmbeddingType;
import com.ai.finderai.models.VectorData;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing stored vector data")
public class VectorDataDTO {

    @Schema(description = "Unique identifier for the vector", example = "8237648273648273")
    private Long id;

    @Schema(description = "AI provider used to generate the embedding", example = "openai")
    private String provider;

    @Schema(description = "Model used for generating the embedding", example = "text-embedding-ada-002")
    private String model;

    @Schema(description = "Type of embedding (TEXT, IMAGE, FILE, etc.)", example = "TEXT")
    private EmbeddingType embeddingType;

    @Schema(description = "Original input data (text, image URL, file path, etc.)", example = "Hello world")
    private String source;

    @Schema(description = "Generated embedding vector", example = "[0.1, 0.2, 0.3, ...]")
    private float[] embedding;

    @Schema(description = "Additional metadata", example = "{\"category\": \"science\"}")
    private Map<String, Object> metadata;

    @Schema(description = "Timestamp when the vector was created", example = "2025-03-19T12:30:00")
    private LocalDateTime createdAt;

    // Constructor to convert entity to DTO
    public VectorDataDTO(VectorData vectorData) {
        this.id = vectorData.getId();
        this.provider = vectorData.getProvider();
        this.model = vectorData.getModel();
        this.embeddingType = vectorData.getEmbeddingType();
        this.source = vectorData.getSource();
        this.embedding = vectorData.getEmbedding();
        this.metadata = vectorData.getMetadata();
        this.createdAt = vectorData.getCreatedAt();
    }
}
