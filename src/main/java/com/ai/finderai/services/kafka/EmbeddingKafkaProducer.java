package com.ai.finderai.services.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ai.finderai.dto.embedding.CsvEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.DatabaseEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.FileEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.ImageEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.TextEmbeddingRequestDTO;

@Service
public class EmbeddingKafkaProducer {
    @Value("${kafka.topic.text-embedding}")
    private String textEmbeddingTopic;

    @Value("${kafka.topic.image-embedding}")
    private String imageEmbeddingTopic;

    @Value("${kafka.topic.csv-embedding}")
    private String csvEmbeddingTopic;

    @Value("${kafka.topic.database-embedding}")
    private String databaseEmbeddingTopic;

    @Value("${kafka.topic.file-embedding}")
    private String fileEmbeddingTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EmbeddingKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTextEmbeddingRequest(TextEmbeddingRequestDTO request) {
        kafkaTemplate.send(textEmbeddingTopic, request);
    }

    public void sendImageEmbeddingRequest(ImageEmbeddingRequestDTO request) {
        kafkaTemplate.send(imageEmbeddingTopic, request);
    }

    public void sendCsvEmbeddingRequest(CsvEmbeddingRequestDTO request) {
        kafkaTemplate.send(csvEmbeddingTopic, request);
    }

    public void sendDatabaseEmbeddingRequest(DatabaseEmbeddingRequestDTO request) {
        kafkaTemplate.send(databaseEmbeddingTopic, request);
    }

    public void sendFileEmbeddingRequest(FileEmbeddingRequestDTO request) {
        kafkaTemplate.send(fileEmbeddingTopic, request);
    }
}
