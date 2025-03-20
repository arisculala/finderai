package com.ai.finderai.services;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.generic.exceptions.InternalServerException;

import jakarta.annotation.PostConstruct;

@Service("huggingface-embedding-service")
public class HuggingFaceEmbeddingService implements EmbeddingService {
    private static final Logger logger = LoggerFactory.getLogger(HuggingFaceEmbeddingService.class);

    private final WebClient.Builder webClientBuilder;
    private WebClient webClient;

    @Value("${embedding.provider.huggingface.api.base-url}")
    private String baseUrl;

    @Value("${embedding.provider.huggingface.api.api-key}")
    private String apiKey;

    @Value("${embedding.provider.huggingface.api.model}")
    private String model;

    public HuggingFaceEmbeddingService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @PostConstruct
    public void initWebClient() {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
        logger.info("HuggingFace WebClient initialized with base URL: {}", baseUrl);
    }

    @SuppressWarnings("unchecked")
    @Override
    public float[] generateEmbedding(String text) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "inputs", text,
                    "options", Map.of("wait_for_model", true));

            List<Double> response = webClient.post()
                    .uri(model)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();

            float[] embeddingArray = new float[response.size()];
            for (int i = 0; i < response.size(); i++) {
                embeddingArray[i] = response.get(i).floatValue();
            }

            return embeddingArray;

        } catch (WebClientResponseException e) {
            logger.error("HuggingFace embeddings API error: {}", e.getResponseBodyAsString());
            throw new InternalServerException("HuggingFace API error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Error generating embedding using HuggingFace API", e);
            throw new InternalServerException("Error generating embedding: " + e.getMessage(), e);
        }
    }
}
