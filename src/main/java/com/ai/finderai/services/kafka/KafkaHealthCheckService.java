package com.ai.finderai.services.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaHealthCheckService {
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    public boolean isKafkaRunning() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        try (AdminClient adminClient = AdminClient.create(props)) {
            adminClient.listTopics().names().get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }
}
