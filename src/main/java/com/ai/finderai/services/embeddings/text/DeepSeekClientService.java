package com.ai.finderai.services.embeddings.text;

import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.generic.exceptions.InternalServerException;

import java.util.List;
import java.util.Map;

/**
 * Service for integrating with the DeepSeek AI API.
 */
@Service("deepseek")
@Schema(description = "Client service for DeepSeek AI API.")
public class DeepSeekClientService implements TextEmbeddingProviderClient {

    private static final Logger logger = LoggerFactory.getLogger(DeepSeekClientService.class);

    private final WebClient webClient;

    @Value("${aiprovider.provider.deepseek.api.base-url}")
    private String baseUrl;

    @Value("${aiprovider.provider.deepseek.api.api-key}")
    private String apiKey;

    @Value("${aiprovider.provider.deepseek.api.model}")
    private String model;

    public DeepSeekClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public float[] generateEmbeddingFromText(String text) {
        try {
            logger.info("Requesting DeepSeek AI embedding for text: {}", text);

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

            logger.info("Successfully retrieved DeepSeek AI embedding.");
            return embeddingArray;

        } catch (WebClientResponseException e) {
            logger.error("DeepSeek API error: {}", e.getResponseBodyAsString());
            throw new InternalServerException("DeepSeek API error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Error generating embedding using DeepSeek API", e);
            throw new InternalServerException("DeepSeek API error: " + e.getMessage(), e);
        }
    }
}
