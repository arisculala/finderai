package com.ai.finderai.services.kafka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ai.finderai.dto.embedding.BulkImageEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.BulkTextEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.CsvEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.DatabaseEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.FileEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.ImageEmbeddingRequestDTO;
import com.ai.finderai.dto.embedding.TextEmbeddingRequestDTO;
import com.ai.finderai.services.EmbeddingService;

@Service
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class EmbeddingKafkaConsumer {

    private final EmbeddingService embeddingService;

    public EmbeddingKafkaConsumer(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @KafkaListener(topics = "${kafka.topic.text-embedding}", groupId = "text-group")
    public void consumeTextEmbedding(BulkTextEmbeddingRequestDTO request) {
        request.getTexts().forEach(text -> {
            embeddingService.generateTextEmbedding(
                    new TextEmbeddingRequestDTO(true, request.getProvider(), request.getModel(), request.getMetadata(),
                            text));
        });
    }

    @KafkaListener(topics = "${kafka.topic.image-embedding}", groupId = "image-group")
    public void consumeImageEmbedding(BulkImageEmbeddingRequestDTO request) {
        request.getImageUrls().forEach(imageUrl -> {
            embeddingService
                    .generateImageEmbedding(new ImageEmbeddingRequestDTO(false, imageUrl, imageUrl, null, imageUrl));
        });
    }

    @KafkaListener(topics = "${kafka.topic.csv-embedding}", groupId = "csv-group")
    public void consumeCsvEmbedding(CsvEmbeddingRequestDTO request) {
        // Load CSV file, process embeddings, and store them
        // List<String> csvTexts = CsvUtils.readCsvFile(request.getCsvFilePath());
        // csvTexts.forEach(text -> {
        // float[] embedding =
        // embeddingService.generateTextEmbedding(request.getProvider(), text);
        // vectorDataService
        // .saveEmbedding(new NewVectorDataRequestDTO(request.getProvider(),
        // request.getModel(), text,
        // request.getMetadata(),
        // embedding));
        // });
    }

    @KafkaListener(topics = "${kafka.topic.database-embedding}", groupId = "db-group")
    public void consumeDatabaseEmbedding(DatabaseEmbeddingRequestDTO request) {
        // Query database, process embeddings, and store them
        // List<String> dbRecords =
        // DatabaseUtils.getDatabaseRecords(request.getDatabaseTable());
        // dbRecords.forEach(record -> {
        // float[] embedding =
        // embeddingService.generateTextEmbedding(request.getProvider(), record);
        // vectorDataService
        // .saveEmbedding(new NewVectorDataRequestDTO(request.getProvider(),
        // request.getModel(), record,
        // request.getMetadata(),
        // embedding));
        // });
    }

    @KafkaListener(topics = "${kafka.topic.file-embedding}", groupId = "file-group")
    public void consumeFileEmbedding(FileEmbeddingRequestDTO request) {
        // Read file content, process embeddings, and store them
        // List<String> fileTexts = FileUtils.readTextFile(request.getFilePath());
        // fileTexts.forEach(text -> {
        // float[] embedding =
        // embeddingService.generateFileEmbedding(request.getProvider(), text);
        // vectorDataService
        // .saveEmbedding(new NewVectorDataRequestDTO(request.getProvider(),
        // request.getModel(), text,
        // request.getMetadata(),
        // embedding));
        // });
    }
}
