package ru.yandex.practicum.telemetry.dto.device;

import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

public enum ScenarioConditionType {
    MOTION, LUMINOSITY, SWITCH, TEMPERATURE, CO2LEVEL, HUMIDITY;

    public ConditionTypeAvro toAvro() {
        switch (this) {
            case MOTION:
                return ConditionTypeAvro.MOTION;
            case CO2LEVEL:
                return ConditionTypeAvro.CO2LEVEL;
            case TEMPERATURE:
                return ConditionTypeAvro.TEMPERATURE;
            case LUMINOSITY:
                return ConditionTypeAvro.LUMINOSITY;
            case SWITCH:
                return ConditionTypeAvro.SWITCH;
            case HUMIDITY:
                return ConditionTypeAvro.HUMIDITY;
            case null:
                throw new IllegalArgumentException("Can't map null to ConditionTypeAvro");
            default:
                throw new IllegalArgumentException(
                        String.format("Can't map ScenarioConditionType::%s to ConditionTypeAvro", this)
                );
        }
    }
}
