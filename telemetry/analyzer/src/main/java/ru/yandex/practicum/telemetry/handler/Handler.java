package ru.yandex.practicum.telemetry.handler;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface Handler {

    void handle(HubEventAvro event);

    HubEventType getType();
}
