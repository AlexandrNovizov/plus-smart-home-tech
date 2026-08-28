package ru.yandex.practicum.telemetry.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.handler.Handler;
import ru.yandex.practicum.telemetry.handler.HubEventType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HubEventServiceImpl implements HubEventService {

    private final Map<HubEventType, Handler> handlers;

    public HubEventServiceImpl(List<Handler> handlersList) {
        this.handlers = handlersList.stream()
                .collect(Collectors.toMap(Handler::getType, Function.identity()));
    }

    @Override
    public void processEvent(HubEventAvro event) {
        log.info("processing event {}", event.toString());
        handlers.get(deduceType(event.getPayload())).handle(event);
    }

    private HubEventType deduceType(Object payload) {
        if (payload instanceof ScenarioAddedEventAvro) {
            return HubEventType.SCENARIO_ADDED;
        }
        if (payload instanceof ScenarioRemovedEventAvro) {
            return HubEventType.SCENARIO_REMOVED;
        }
        if (payload instanceof DeviceAddedEventAvro) {
            return HubEventType.DEVICE_ADDED;
        }
        if (payload instanceof DeviceRemovedEventAvro) {
            return HubEventType.DEVICE_REMOVED;
        }

        throw new IllegalArgumentException("Can't find HubEventType enum value for " + payload);
    }
}
