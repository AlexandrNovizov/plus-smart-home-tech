package ru.yandex.practicum.telemetry.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.repository.ScenarioRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioRemovedHandler implements Handler {

    private final ScenarioRepository scenarioRepository;

    @Override
    public void handle(HubEventAvro event) {
        ScenarioRemovedEventAvro scenarioRemoved = (ScenarioRemovedEventAvro) event.getPayload();
        scenarioRepository.deleteByHubIdAndName(event.getHubId(), scenarioRemoved.getName());
    }

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
