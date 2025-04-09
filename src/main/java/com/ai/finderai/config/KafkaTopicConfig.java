package com.ai.finderai.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic textEmbeddingTopic() {
        return TopicBuilder.name("text-embedding-topic").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic imageEmbeddingTopic() {
        return TopicBuilder.name("image-embedding-topic").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic csvEmbeddingTopic() {
        return TopicBuilder.name("csv-embedding-topic").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic databaseEmbeddingTopic() {
        return TopicBuilder.name("database-embedding-topic").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic fileEmbeddingTopic() {
        return TopicBuilder.name("file-embedding-topic").partitions(3).replicas(1).build();
    }
}
