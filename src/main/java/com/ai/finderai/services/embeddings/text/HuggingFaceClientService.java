package com.ai.finderai.services.embeddings.text;

import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.generic.exceptions.InternalServerException;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Service for integrating with the Hugging Face AI API using RestTemplate.
 */
@Service("huggingface")
@Schema(description = "Client service for Hugging Face AI API.")
public class HuggingFaceClientService implements TextEmbeddingProviderClient {

    private static final Logger logger = LoggerFactory.getLogger(HuggingFaceClientService.class);

    private final RestTemplate restTemplate;

    @Value("${aiprovider.provider.huggingface.api.base-url}")
    private String baseUrl;

    @Value("${aiprovider.provider.huggingface.api.api-key}")
    private String apiKey;

    @Value("${aiprovider.provider.huggingface.api.model}")
    private String model;

    public HuggingFaceClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Initialize RestTemplate configuration
     */
    @PostConstruct
    public void init() {
        logger.info("HuggingFace API Client initialized with base URL: {}", baseUrl);
    }

    @SuppressWarnings("unchecked")
    @Override
    public float[] generateEmbeddingFromText(String text) {
        try {
            logger.info("Requesting Hugging Face embedding for text: {}", text);

            // Construct the request body
            Map<String, Object> requestBody = Map.of(
                    "inputs", text,
                    "options", Map.of("wait_for_model", true));

            // Prepare the HTTP headers with authorization and content type
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");

            // Construct the HttpEntity object with the request body and headers
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Send the POST request
            ResponseEntity<List> responseEntity = restTemplate.exchange(
                    baseUrl + model,
                    HttpMethod.POST,
                    entity,
                    List.class);

            // Get the response body (embedding)
            List<Double> response = responseEntity.getBody();

            // Convert the response into a float array
            float[] embeddingArray = new float[response.size()];
            for (int i = 0; i < response.size(); i++) {
                embeddingArray[i] = response.get(i).floatValue();
            }

            logger.info("Successfully retrieved Hugging Face embedding.");
            return embeddingArray;

        } catch (Exception e) {
            logger.error("Error generating embedding using Hugging Face API", e);
            throw new InternalServerException("Hugging Face API error: " + e.getMessage(), e);
        }
    }
}
