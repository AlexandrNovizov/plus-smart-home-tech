package ru.yandex.practicum.telemetry.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.telemetry.model.Sensor;

@UtilityClass
public class AvroToSensorMapper {

    public static Sensor toEntity(String hubId, DeviceAddedEventAvro event) {
        Sensor sensor = new Sensor();

        sensor.setId(event.getId());
        sensor.setHubId(hubId);

        return sensor;
    }
}
