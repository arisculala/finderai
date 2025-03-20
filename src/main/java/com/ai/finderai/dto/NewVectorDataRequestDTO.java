package com.ai.finderai.dto;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewVectorDataRequestDTO {

    @NotBlank
    private String provider; // e.g., "huggingface", "openai", etc.

    @NotBlank
    private String model; // e.g., "text-embedding-ada-002"

    @NotBlank
    private String text;

    @NotNull
    private Map<String, Object> metadata;
}
