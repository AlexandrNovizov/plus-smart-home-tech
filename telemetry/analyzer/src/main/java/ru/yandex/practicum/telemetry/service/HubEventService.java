package ru.yandex.practicum.telemetry.service;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventService {

    void processEvent(HubEventAvro event);
}
