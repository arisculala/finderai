package com.ai.finderai.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.ai.finderai.models.VectorData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VectorDataDTO {
    private Long id;
    private String provider;
    private String model;
    private String text;
    private float[] embedding;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;

    public VectorDataDTO(VectorData vectorData) {
        this.id = vectorData.getId();
        this.provider = vectorData.getProvider();
        this.model = vectorData.getModel();
        this.text = vectorData.getText();
        this.embedding = vectorData.getEmbedding();
        this.metadata = vectorData.getMetadata();
        this.createdAt = vectorData.getCreatedAt();
    }
}
