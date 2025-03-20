package com.ai.finderai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO containing the generated embedding")
public class EmbeddingResponseDTO {

    @Schema(description = "AI provider used for generating the embedding", example = "openai")
    private String provider;

    @Schema(description = "AI model used for generating the embedding", example = "text-embedding-ada-002")
    private String model;

    @Schema(description = "Generated embedding vector", example = "[-0.021, 0.324, -0.98, 0.45]")
    private float[] embedding;

    @Schema(description = "The number of dimensions in the embedding vector", example = "1536")
    private int dimensions;
}
