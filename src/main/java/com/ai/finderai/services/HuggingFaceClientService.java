package com.ai.finderai.services;

import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.generic.exceptions.InternalServerException;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Service for integrating with the Hugging Face AI API.
 */
@Service("huggingface")
@Schema(description = "Client service for Hugging Face AI API.")
public class HuggingFaceClientService implements AIProviderClient {

    private static final Logger logger = LoggerFactory.getLogger(HuggingFaceClientService.class);

    private final WebClient.Builder webClientBuilder;
    private WebClient webClient;

    @Value("${aiprovider.provider.huggingface.api.base-url}")
    private String baseUrl;

    @Value("${aiprovider.provider.huggingface.api.api-key}")
    private String apiKey;

    @Value("${aiprovider.provider.huggingface.api.model}")
    private String model;

    public HuggingFaceClientService(WebClient.Builder webClientBuilder) {
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
            logger.info("Requesting Hugging Face embedding for text: {}", text);

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

            logger.info("Successfully retrieved Hugging Face embedding.");
            return embeddingArray;

        } catch (WebClientResponseException e) {
            logger.error("Hugging Face API error: {}", e.getResponseBodyAsString());
            throw new InternalServerException("Hugging Face API error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Error generating embedding using Hugging Face API", e);
            throw new InternalServerException("Hugging Face API error: " + e.getMessage(), e);
        }
    }
}
