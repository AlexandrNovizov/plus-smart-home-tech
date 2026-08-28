package ru.yandex.practicum.telemetry.handler.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubEventHandler {

    HubEventProto.PayloadCase getType();

    void handle(HubEventProto event);
}
