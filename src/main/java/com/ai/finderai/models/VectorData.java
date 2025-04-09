package com.ai.finderai.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.ai.finderai.enums.EmbeddingType;
import com.generic.utils.SnowflakeIdGenerator;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entity representing vectorized data stored in the database")
public class VectorData {

    @Id
    @Schema(description = "Unique identifier for the vector", example = "8237648273648273")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "AI provider used to generate the embedding", example = "openai")
    private String provider;

    @Column(nullable = false)
    @Schema(description = "Model used for generating the embedding", example = "text-embedding-ada-002")
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Type of embedding (TEXT, IMAGE, FILE, etc.)", example = "TEXT")
    private EmbeddingType embeddingType;

    @Column(nullable = false)
    @Schema(description = "Original input data (text, image URL, file path, etc.)", example = "Hello world")
    private String source;

    @Column(nullable = false, columnDefinition = "vector(1536)")
    private float[] embedding;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Schema(description = "Additional metadata", example = "{\"category\": \"science\"}")
    private Map<String, Object> metadata;

    @Column(nullable = false, updatable = false)
    @Schema(description = "Timestamp when the vector was created", example = "2025-03-19T12:30:00")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = SnowflakeIdGenerator.getInstance(1).nextId();
        }
        this.createdAt = LocalDateTime.now();
    }
}
