package com.ai.finderai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

import com.ai.finderai.models.ImageVectorData;
import com.ai.finderai.models.TextVectorData;
import com.ai.finderai.models.VectorData;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing vector data stored in the database")
public class VectorDataDTO {

    @Schema(description = "Unique identifier of the vector", example = "123")
    private Long id;

    @Schema(description = "AI provider used for embedding generation", example = "openai")
    private String provider;

    @Schema(description = "Model used for embedding generation", example = "text-embedding-ada-002")
    private String model;

    @Schema(description = "Original text that was embedded", example = "This is a test sentence.")
    private String text;

    @Schema(description = "Generated embedding vector", example = "[-0.021, 0.324, -0.98, 0.45]")
    private float[] embedding;

    @Schema(description = "Additional metadata associated with the vector", example = "{\"category\": \"science\", \"source\": \"Wikipedia\"}")
    private Map<String, Object> metadata;

    @Schema(description = "Timestamp when the vector was created", example = "2025-03-19T12:30:00")
    private LocalDateTime createdAt;

    public VectorDataDTO(VectorData vectorData) {
        this.id = vectorData.getId();
        this.provider = vectorData.getProvider();
        this.model = vectorData.getModel();
        this.metadata = vectorData.getMetadata();
        this.createdAt = vectorData.getCreatedAt();

        // Check if the vectorData is a TextVectorData instance
        if (vectorData instanceof TextVectorData textVector) {
            this.text = textVector.getText();
            this.embedding = textVector.getEmbedding();
        }

        // Check if the vectorData is an ImageVectorData instance
        if (vectorData instanceof ImageVectorData imageVector) {
            this.text = imageVector.getImageUrl(); // Assigning imageUrl to text field (if needed)
            this.embedding = imageVector.getEmbedding();
        }
    }
}
