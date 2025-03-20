package com.ai.finderai.services;

public interface AIProviderClient {
    float[] generateEmbedding(String text);
}