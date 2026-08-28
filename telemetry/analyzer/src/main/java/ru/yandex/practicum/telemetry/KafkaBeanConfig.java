package ru.yandex.practicum.telemetry;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KafkaBeanConfig {

    private final KafkaConfig kafkaConfig;

    @Bean(name = "snapshotConsumer")
    public Consumer<String, SpecificRecordBase> snapshotConsumer() {
        return new KafkaConsumer<>(kafkaConfig.getSnapshots());
    }

    @Bean(name = "hubConsumer")
    public Consumer<String, SpecificRecordBase> hubConsumer() {
        return new KafkaConsumer<>(kafkaConfig.getHub());
    }
}
