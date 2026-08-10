package ru.yandex.practicum.telemetry.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.dto.event.hub.HubEventType;
import ru.yandex.practicum.telemetry.handler.hub.HubEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HubEventServiceImpl implements HubEventService {

    private final Map<HubEventType, HubEventHandler> handlers;

    public HubEventServiceImpl(List<HubEventHandler> handlers) {

        Map<HubEventType, HubEventHandler> handlerMap = handlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getType, Function.identity()));

        this.handlers = handlerMap;
    }

    @Override
    public void send(HubEvent event) {
        HubEventHandler handler = handlers.get(event.getType());
        handler.handle(event);
    }
}
