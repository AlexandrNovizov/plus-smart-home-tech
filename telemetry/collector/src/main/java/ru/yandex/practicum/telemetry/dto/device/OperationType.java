package ru.yandex.practicum.telemetry.dto.device;

import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;

public enum OperationType {
    EQUALS, GREATER_THAN, LOWER_THAN;

    public ConditionOperationAvro toAvro() {
        switch (this) {
            case EQUALS:
                return ConditionOperationAvro.EQUALS;
            case LOWER_THAN:
                return ConditionOperationAvro.LOWER_THAN;
            case GREATER_THAN:
                return ConditionOperationAvro.GREATER_THAN;
            case null:
                throw new IllegalArgumentException("Can't map null to ConditionOperationAvro");
            default:
                throw new IllegalArgumentException(
                        String.format("Can't map OperationType::%s to ConditionOperationAvro", this)
                );
        }
    }
}
