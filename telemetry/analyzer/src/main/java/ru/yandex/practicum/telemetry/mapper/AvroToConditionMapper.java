package ru.yandex.practicum.telemetry.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.model.Condition;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class AvroToConditionMapper {

    public static Map<String, Condition> mapToEntity(Collection<ScenarioConditionAvro> conditions) {
        return conditions.stream()
                .collect(Collectors.toMap(
                                ScenarioConditionAvro::getSensorId,
                                AvroToConditionMapper::mapToEntity)
                );
    }

    public static Condition mapToEntity(ScenarioConditionAvro avro) {
        Condition condition = new Condition();
        condition.setType(AvroEnumMapper.toEntity(avro.getType()));
        condition.setOperation(AvroEnumMapper.toEntity(avro.getOperation()));
        Object value = avro.getValue();
        if (value instanceof Boolean) {
            condition.setValue((Boolean) value ? 1 : 0);
        } else if (value instanceof Integer) {
            condition.setValue((Integer) value);
        }
        return condition;
    }
}
