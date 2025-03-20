package com.ai.finderai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddingResponseDTO {
    private String provider;
    private String model;
    private float[] embedding;
    private int dimensions;
}
