package ru.yandex.practicum.telemetry.dto.device.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.telemetry.dto.device.DeviceAction;

@UtilityClass
public class DeviceActionMapper {

    public static DeviceActionAvro mapToRecord(DeviceAction action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(action.getType().mapToAvro())
                .setValue(action.getValue())
                .build();
    }
}
