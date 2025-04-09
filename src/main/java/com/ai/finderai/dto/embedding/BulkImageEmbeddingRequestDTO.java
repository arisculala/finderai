package com.ai.finderai.dto.embedding;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for generating image embeddings")
public class BulkImageEmbeddingRequestDTO {
    private boolean save = false; // default to false
    private String provider;
    private String model;
    private Map<String, Object> metadata;
    private List<String> imageUrls;
}
