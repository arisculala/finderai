package com.ai.finderai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for storing a new vector in the database")
public class NewVectorDataRequestDTO {

    @NotBlank
    @Schema(description = "AI provider used for generating the embedding", example = "openai")
    private String provider;

    @NotBlank
    @Schema(description = "Model used for generating the embedding", example = "text-embedding-ada-002")
    private String model;

    @NotBlank
    @Schema(description = "Original text that was embedded", example = "This is a test sentence.")
    private String text;

    @NotNull
    @Schema(description = "Additional metadata associated with the vector", example = "{\"category\": \"science\", \"source\": \"Wikipedia\"}")
    private Map<String, Object> metadata;
}
