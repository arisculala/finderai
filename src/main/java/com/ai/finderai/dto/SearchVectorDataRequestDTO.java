package com.ai.finderai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchVectorDataRequestDTO {

    @NotBlank
    private String provider; // e.g., "huggingface", "openai", etc.

    @NotBlank
    private String model; // e.g., "text-embedding-ada-002"

    @NotBlank
    private String query;

    @NotBlank
    private int limit;
}
