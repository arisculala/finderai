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
public class GetEmbeddingRequestDTO {
    @NotBlank
    private String provider;

    @NotBlank
    private String model;

    @NotBlank
    private String text;
}
