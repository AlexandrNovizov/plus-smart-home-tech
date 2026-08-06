package ru.yandex.practicum.telemetry.kafka;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class KafkaEventProducerImpl implements KafkaEventProducer {

    private final Producer<String, SpecificRecordBase> producer;

    @Value("${hub.event.topic.name}")
    private String HUB_TOPIC;
    @Value("${sensor.event.topic.name}")
    private String SENSOR_TOPIC;

    @Override
    public void send(HubEventAvro hubEventAvro) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                HUB_TOPIC,
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
                SENSOR_TOPIC,
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
