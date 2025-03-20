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

@Service("openai-embedding-service")
public class OpenAIEmbeddingService implements EmbeddingService {
    private static final Logger logger = LoggerFactory.getLogger(OpenAIEmbeddingService.class);

    private final WebClient webClient;

    @Value("${embedding.provider.openai.api.base-url}")
    private String baseUrl;

    @Value("${embedding.provider.openai.api.key}")
    private String apiKey;

    @Value("${embedding.provider.openai.api.model}")
    private String model;

    public OpenAIEmbeddingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public float[] generateEmbedding(String text) {
        try {
            Map<String, Object> response = webClient.post()
                    .uri("/embeddings")
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(Map.of("model", model, "input", text))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List<Double> embeddingList = (List<Double>) ((Map<?, ?>) ((List<?>) response.get("data")).get(0))
                    .get("embedding");

            float[] embeddingArray = new float[embeddingList.size()];
            for (int i = 0; i < embeddingList.size(); i++) {
                embeddingArray[i] = embeddingList.get(i).floatValue();
            }
            return embeddingArray;

        } catch (WebClientResponseException e) {
            logger.error("HuggingFace embeddings API error: {}", e);
            throw new InternalServerException("OpenAI embeddings API error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Error encountered generation embedding using OpenAI API: {}", e);
            throw new InternalServerException(
                    "Error encountered generation embedding using OpenAI API: " + e.getMessage(), e);
        }
    }
}
