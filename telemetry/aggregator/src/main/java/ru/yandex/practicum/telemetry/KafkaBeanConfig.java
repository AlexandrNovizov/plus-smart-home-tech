package ru.yandex.practicum.telemetry;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.telemetry.kafka.EventSerializer;
import ru.yandex.practicum.telemetry.kafka.SensorEventDeserializer;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaBeanConfig {

    private final KafkaConfig kafkaConfig;

    @Bean
    public Producer<String, SpecificRecordBase> producer() {
        return new KafkaProducer<>(kafkaConfig.getProducer());
    }

    @Bean
    public Consumer<String, SpecificRecordBase> consumer() {
        return new KafkaConsumer<>(kafkaConfig.getConsumer());
    }
}
