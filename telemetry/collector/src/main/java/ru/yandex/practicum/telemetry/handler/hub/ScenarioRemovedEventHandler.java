package ru.yandex.practicum.telemetry.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.dto.event.hub.HubEventType;
import ru.yandex.practicum.telemetry.dto.event.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.telemetry.kafka.KafkaEventProducer;

@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {

    public ScenarioRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public ScenarioRemovedEventAvro mapToAvro(HubEvent event) {
        if (!(event instanceof ScenarioRemovedEvent)) {
            throw new IllegalArgumentException(String.format("Can't map %s to ScenarioRemovedEvent", event.getType().name()));
        }

        ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) event;

        return ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEvent.getName())
                .build();
    }

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
