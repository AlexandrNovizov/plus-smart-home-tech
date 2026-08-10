package ru.yandex.practicum.telemetry.handler.hub;

import ru.yandex.practicum.telemetry.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.dto.event.hub.HubEventType;

public interface HubEventHandler {

    HubEventType getType();

    void handle(HubEvent event);
}
