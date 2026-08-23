package ru.yandex.practicum.telemetry.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.telemetry.model.Action;
import ru.yandex.practicum.telemetry.model.Condition;
import ru.yandex.practicum.telemetry.model.Scenario;


import java.util.Map;

@UtilityClass
public class AvroToScenarioMapper {

    public static Scenario mapToEntity(String hubId, ScenarioAddedEventAvro avro,
                                       Map<String, Condition> conditions, Map<String, Action> actions) {

        Scenario entity = new Scenario();

        entity.setHubId(hubId);
        entity.setName(avro.getName());
        entity.setConditions(conditions);
        entity.setActions(actions);

        return entity;
    }
}
