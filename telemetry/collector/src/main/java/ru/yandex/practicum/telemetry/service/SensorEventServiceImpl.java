package ru.yandex.practicum.telemetry.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.dto.event.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.dto.event.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.handler.sensor.SensorEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SensorEventServiceImpl implements SensorEventService {

    private final Map<SensorEventType, SensorEventHandler> handlers;

    public SensorEventServiceImpl(List<SensorEventHandler> handlers) {

        Map<SensorEventType, SensorEventHandler> handlerMap = handlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getType, Function.identity()));

        this.handlers = handlerMap;
    }

    @Override
    public void send(SensorEvent event) {
        SensorEventHandler handler = handlers.get(event.getType());
        handler.handle(event);
    }
}
