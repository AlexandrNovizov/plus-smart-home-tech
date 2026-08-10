package ru.yandex.practicum.telemetry.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.dto.device.mapper.DeviceActionMapper;
import ru.yandex.practicum.telemetry.dto.device.mapper.ScenarioConditionMapper;
import ru.yandex.practicum.telemetry.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.dto.event.hub.HubEventType;
import ru.yandex.practicum.telemetry.dto.event.hub.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.kafka.KafkaEventProducer;

import java.util.List;

@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public ScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public ScenarioAddedEventAvro mapToAvro(HubEvent event) {
        if (!(event instanceof ScenarioAddedEvent)) {
            throw new IllegalArgumentException(String.format("Can't map %s to ScenarioAddedEvent", event.getType().name()));
        }

        ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) event;

        List<DeviceActionAvro> actions = scenarioAddedEvent.getActions().stream()
                .map(DeviceActionMapper::mapToRecord)
                .toList();

        List<ScenarioConditionAvro> conditions = scenarioAddedEvent.getConditions().stream()
                .map(ScenarioConditionMapper::mapToRecord)
                .toList();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setActions(actions)
                .setConditions(conditions)
                .build();
    }

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
