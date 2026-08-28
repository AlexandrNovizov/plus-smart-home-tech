package ru.yandex.practicum.telemetry.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.telemetry.model.ActionType;
import ru.yandex.practicum.telemetry.model.ConditionType;
import ru.yandex.practicum.telemetry.model.Operation;

@UtilityClass
public class AvroEnumMapper {

    public static ConditionType toEntity(ConditionTypeAvro avro) {
        switch (avro) {
            case HUMIDITY:
                return ConditionType.HUMIDITY;
            case SWITCH:
                return ConditionType.SWITCH;
            case LUMINOSITY:
                return ConditionType.LUMINOSITY;
            case TEMPERATURE:
                return ConditionType.TEMPERATURE;
            case CO2LEVEL:
                return ConditionType.CO2LEVEL;
            case MOTION:
                return ConditionType.MOTION;
            case null, default:
                throw new IllegalArgumentException("Can't find ConditionType mapper for " +
                        (avro == null ? null : avro.name()));
        }
    }

    public static Operation toEntity(ConditionOperationAvro avro) {
        switch (avro) {
            case EQUALS:
                return Operation.EQUALS;
            case LOWER_THAN:
                return Operation.LOWER_THAN;
            case GREATER_THAN:
                return Operation.GREATER_THAN;
            case null, default:
                throw new IllegalArgumentException("Can't find Operation mapper for " +
                        (avro == null ? null : avro.name()));
        }
    }

    public static ActionType toEntity(ActionTypeAvro avro) {
        switch (avro) {
            case INVERSE:
                return ActionType.INVERSE;
            case DEACTIVATE:
                return ActionType.DEACTIVATE;
            case ACTIVATE:
                return ActionType.ACTIVATE;
            case SET_VALUE:
                return ActionType.SET_VALUE;
            case null, default:
                throw new IllegalArgumentException("Can't find ActionType mapper for " +
                        (avro == null ? null : avro.name()));
        }
    }
}
