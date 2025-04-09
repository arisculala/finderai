package com.ai.finderai.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.finderai.services.kafka.KafkaHealthCheckService;

@RestController
@RequestMapping("/api/v1/healthcheck")
public class HealthCheckController {
    private final KafkaHealthCheckService kafkaHealthCheckService;

    public HealthCheckController(KafkaHealthCheckService kafkaHealthCheckService) {
        this.kafkaHealthCheckService = kafkaHealthCheckService;
    }

    @GetMapping("/kafka")
    public String checkKafkaHealth() {
        return kafkaHealthCheckService.isKafkaRunning() ? "Kafka is running!" : "Kafka is down!";
    }
}
