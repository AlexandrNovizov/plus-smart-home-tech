package ru.yandex.practicum.telemetry.service;

import ru.yandex.practicum.telemetry.dto.event.hub.HubEvent;

public interface HubEventService {
    void send(HubEvent event);
}
