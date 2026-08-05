package ru.yandex.practicum.telemetry.dto.device;

import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

public enum DeviceType {
    MOTION_SENSOR, TEMPERATURE_SENSOR, LIGHT_SENSOR, CLIMATE_SENSOR, SWITCH_SENSOR;

    public DeviceTypeAvro toAvro() {
        switch (this) {
            case LIGHT_SENSOR:
                return DeviceTypeAvro.LIGHT_SENSOR;
            case MOTION_SENSOR:
                return DeviceTypeAvro.MOTION_SENSOR;
            case SWITCH_SENSOR:
                return DeviceTypeAvro.SWITCH_SENSOR;
            case CLIMATE_SENSOR:
                return DeviceTypeAvro.CLIMATE_SENSOR;
            case TEMPERATURE_SENSOR:
                return DeviceTypeAvro.TEMPERATURE_SENSOR;
            case null:
                throw new IllegalArgumentException("Can't map null to DeviceTypeAvro");
            default:
                throw new IllegalArgumentException(String.format("Can't map DeviceType::%s to DeviceTypeAvro", this));
        }
    }
}
