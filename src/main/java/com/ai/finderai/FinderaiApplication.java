package com.ai.finderai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "FinderAI Service API", version = "0.0.1", description = "API for FinderAI Service"))
@SpringBootApplication(scanBasePackages = { "com.ai", "com.generic" })
public class FinderaiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinderaiApplication.class, args);
    }

}
