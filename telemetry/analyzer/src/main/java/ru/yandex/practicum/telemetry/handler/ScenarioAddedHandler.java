package ru.yandex.practicum.telemetry.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.telemetry.mapper.AvroToActionMapper;
import ru.yandex.practicum.telemetry.mapper.AvroToConditionMapper;
import ru.yandex.practicum.telemetry.mapper.AvroToScenarioMapper;
import ru.yandex.practicum.telemetry.model.Action;
import ru.yandex.practicum.telemetry.model.Condition;
import ru.yandex.practicum.telemetry.model.Scenario;
import ru.yandex.practicum.telemetry.repository.ActionRepository;
import ru.yandex.practicum.telemetry.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.repository.ScenarioRepository;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioAddedHandler implements Handler {

    private final ScenarioRepository scenarioRepository;

    @Override
    public void handle(HubEventAvro event) {
        ScenarioAddedEventAvro scenarioAdded = (ScenarioAddedEventAvro) event.getPayload();
        Map<String, Condition> conditions = AvroToConditionMapper.mapToEntity(scenarioAdded.getConditions());

        Map<String, Action> actions = AvroToActionMapper.toEntity(scenarioAdded.getActions());

        Scenario scenario = AvroToScenarioMapper.mapToEntity(event.getHubId(), scenarioAdded, conditions, actions);
        scenarioRepository.save(scenario);
    }

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
