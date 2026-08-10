package ru.yandex.practicum.telemetry.service;

import ru.yandex.practicum.telemetry.dto.event.sensor.SensorEvent;

public interface SensorEventService {

    void send(SensorEvent event);
}
