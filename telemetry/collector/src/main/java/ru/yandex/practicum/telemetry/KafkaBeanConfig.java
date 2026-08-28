package ru.yandex.practicum.telemetry;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KafkaBeanConfig {

    private final KafkaConfig kafkaConfig;

    @Bean
    public Producer<String, SpecificRecordBase> producer() {
        return new KafkaProducer<>(kafkaConfig.getProperties());
    }
}
