package com.ai.finderai.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("TEXT")
@Getter
@Setter
public class TextVectorData extends VectorData {
    @Column(nullable = false)
    private String text;

    @Column(nullable = false, columnDefinition = "vector(1536)")
    private float[] embedding;
}
