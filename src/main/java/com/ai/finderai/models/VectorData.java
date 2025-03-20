package com.ai.finderai.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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

    @Column(nullable = false)
    @Schema(description = "Original text associated with the embedding", example = "This is a sample text.")
    private String text;

    @Column(nullable = false, columnDefinition = "vector(1536)")
    @Schema(description = "Generated embedding vector", example = "[-0.021, 0.324, -0.98, 0.45]")
    private float[] embedding;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Schema(description = "Additional metadata associated with the vector", example = "{\"category\": \"science\", \"source\": \"Wikipedia\"}")
    private Map<String, Object> metadata;

    @Column(nullable = false, updatable = false)
    @Schema(description = "Timestamp when the vector data was created", example = "2025-03-19T12:30:00")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = SnowflakeIdGenerator.getInstance(1).nextId();
        }
        this.createdAt = LocalDateTime.now();
    }
}
