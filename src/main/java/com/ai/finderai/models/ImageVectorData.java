package com.ai.finderai.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("IMAGE")
@Getter
@Setter
public class ImageVectorData extends VectorData {
    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false, columnDefinition = "vector(1024)")
    private float[] embedding;
}
