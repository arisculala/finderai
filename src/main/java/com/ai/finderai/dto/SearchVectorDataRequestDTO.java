package com.ai.finderai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for searching vector data based on text query")
public class SearchVectorDataRequestDTO {

    @NotBlank
    @Schema(description = "AI provider used for generating embeddings", example = "openai")
    private String provider;

    @NotBlank
    @Schema(description = "Model used for generating embeddings", example = "text-embedding-ada-002")
    private String model;

    @NotBlank
    @Schema(description = "Query text for similarity search", example = "Machine learning basics")
    private String query;

    @Min(1)
    @Schema(description = "Number of closest records to return", example = "10")
    private int limit;

    @Schema(description = "Types of vector data to search (e.g., text, image)", example = "[\"text\", \"image\"]")
    private Set<String> types;
}
