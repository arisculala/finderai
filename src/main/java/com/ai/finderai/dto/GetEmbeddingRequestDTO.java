package com.ai.finderai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for generating a text embedding")
public class GetEmbeddingRequestDTO {

    @NotBlank
    @Schema(description = "AI provider for embedding generation", example = "openai")
    private String provider;

    @NotBlank
    @Schema(description = "Model used for embedding", example = "text-embedding-ada-002")
    private String model;

    @NotBlank
    @Schema(description = "Text input for which embedding is generated", example = "Hello, how are you?")
    private String text;
}
