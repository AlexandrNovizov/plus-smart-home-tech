package ru.yandex.practicum.telemetry.handler.sensor;

import ru.yandex.practicum.telemetry.dto.event.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.dto.event.sensor.SensorEventType;

public interface SensorEventHandler {

    SensorEventType getType();

    void handle(SensorEvent event);
}
