package ru.yandex.practicum.telemetry.dto.device.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.dto.device.ScenarioCondition;

@UtilityClass
public class ScenarioConditionMapper {

    public static ScenarioConditionAvro mapToRecord(ScenarioCondition condition) {

        Object value = condition.getValue();

        if (value != null) {
            boolean isTypeBoundariesSatisfied = value instanceof Integer || value instanceof Boolean;
            if (!isTypeBoundariesSatisfied) {
                throw new IllegalStateException("Value doesn't satisfy the type boundaries: " + value);
            }
        }

        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(condition.getType().toAvro())
                .setOperation(condition.getOperation().toAvro())
                .setValue(value)
                .build();
    }
}
