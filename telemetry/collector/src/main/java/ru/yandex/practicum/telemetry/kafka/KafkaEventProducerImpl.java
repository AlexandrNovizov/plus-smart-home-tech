package ru.yandex.practicum.telemetry.kafka;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.KafkaConfig;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class KafkaEventProducerImpl implements KafkaEventProducer {

    private final Producer<String, SpecificRecordBase> producer;
    private final KafkaConfig kafkaConfig;

    private static final String HUB_TOPIC = "hub.event";
    private static final String SENSOR_TOPIC = "sensor.event";

    @Override
    public void send(HubEventAvro hubEventAvro) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                kafkaConfig.getTopics().getProperty(HUB_TOPIC),
                null,
                hubEventAvro.getTimestamp().toEpochMilli(),
                hubEventAvro.getHubId(),
                hubEventAvro
        );
        producer.send(record);
    }

    @Override
    public void send(SensorEventAvro sensorEventAvro) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                kafkaConfig.getTopics().getProperty(SENSOR_TOPIC),
                null,
                sensorEventAvro.getTimestamp().toEpochMilli(),
                sensorEventAvro.getHubId(),
                sensorEventAvro
        );
        producer.send(record);
    }

    @Override
    @PreDestroy
    public void close() {
        producer.flush();
        producer.close(Duration.ofSeconds(10));
    }
}
