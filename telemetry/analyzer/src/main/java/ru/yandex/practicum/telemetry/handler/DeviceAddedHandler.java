package ru.yandex.practicum.telemetry.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.mapper.AvroToSensorMapper;
import ru.yandex.practicum.telemetry.model.Sensor;
import ru.yandex.practicum.telemetry.repository.SensorRepository;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceAddedHandler implements Handler {

    private final SensorRepository sensorRepository;

    @Override
    public void handle(HubEventAvro event) {

        DeviceAddedEventAvro device = (DeviceAddedEventAvro) event.getPayload();
        Optional<Sensor> optSensor = sensorRepository.findByIdAndHubId(device.getId(), event.getHubId());
        if (optSensor.isPresent()) {
            log.error("Device with id '{}' already exists", device.getId());
            return;
        }

        Sensor sensor = AvroToSensorMapper.toEntity(event.getHubId(), device);
        sensorRepository.save(sensor);
    }

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
