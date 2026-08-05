package ru.yandex.practicum.telemetry.dto.device.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.dto.device.ScenarioCondition;

@UtilityClass
public class ScenarioConditionMapper {

    public static ScenarioConditionAvro mapToRecord(ScenarioCondition condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(condition.getType().toAvro())
                .setOperation(condition.getOperation().toAvro())
                .setValue(condition.getValue())
                .build();
    }
}
