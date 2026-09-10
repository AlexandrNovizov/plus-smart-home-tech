package ru.yandex.practicum.telemetry;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaBeanConfig {

    private final KafkaConfig kafkaConfig;

    @Bean(name = "snapshotConsumer")
    public Consumer<String, SpecificRecordBase> snapshotConsumer() {
        Properties properties = kafkaConfig.getSnapshots();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        return new KafkaConsumer<>(properties);
    }

    @Bean(name = "hubConsumer")
    public Consumer<String, SpecificRecordBase> hubConsumer() {
        Properties properties = kafkaConfig.getHub();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        return new KafkaConsumer<>(properties);
    }
}
