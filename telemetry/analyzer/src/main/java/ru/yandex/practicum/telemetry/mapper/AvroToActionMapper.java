package ru.yandex.practicum.telemetry.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.telemetry.model.Action;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class AvroToActionMapper {

    public static Map<String, Action> toEntity(Collection<DeviceActionAvro> devices) {
        return devices.stream()
                .collect(Collectors.toMap(
                        DeviceActionAvro::getSensorId,
                        AvroToActionMapper::toEntity)
                );
    }

    public static Action toEntity(DeviceActionAvro avro) {
        Action action = new Action();

        action.setType(AvroEnumMapper.toEntity(avro.getType()));
        action.setValue(avro.getValue());

        return action;
    }
}
