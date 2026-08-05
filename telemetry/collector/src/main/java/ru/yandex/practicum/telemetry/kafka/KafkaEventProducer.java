package ru.yandex.practicum.telemetry.kafka;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface KafkaEventProducer {

    void send(HubEventAvro hubEventAvro);

    void send(SensorEventAvro sensorEventAvro);

    void close();
}
