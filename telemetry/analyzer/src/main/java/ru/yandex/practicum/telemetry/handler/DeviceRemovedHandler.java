package ru.yandex.practicum.telemetry.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceRemovedHandler implements Handler {

    private final SensorRepository sensorRepository;

    @Override
    public void handle(HubEventAvro event) {
        DeviceRemovedEventAvro deviceRemoved = (DeviceRemovedEventAvro) event.getPayload();
        sensorRepository.deleteById(deviceRemoved.getId());
    }

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
