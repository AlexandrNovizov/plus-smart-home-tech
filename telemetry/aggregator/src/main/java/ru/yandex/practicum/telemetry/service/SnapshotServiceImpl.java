package ru.yandex.practicum.telemetry.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class SnapshotServiceImpl implements SnapshotService {

    private final Map<String, SensorsSnapshotAvro> lastSnapshots = new HashMap<>();

    @Override
    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro lastHubSnapshot = lastSnapshots.getOrDefault(
                event.getHubId(),
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(event.getHubId())
                        .setSensorsState(new HashMap<>())
                        .setTimestamp(event.getTimestamp())
                        .build()
        );

        Map<String, SensorStateAvro> sensorsState = lastHubSnapshot.getSensorsState();

        if (isStateUpToDate(sensorsState, event)) {
            log.info("Snapshot data is up to date");
            return Optional.empty();
        }

        log.info("Data is changed, creating new snapshot");
        SensorStateAvro sensorState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        sensorsState.put(event.getId(), sensorState);
        lastHubSnapshot.setTimestamp(event.getTimestamp());
        lastSnapshots.put(event.getHubId(), lastHubSnapshot);
        return Optional.of(lastHubSnapshot);
    }

    private boolean isStateUpToDate(Map<String, SensorStateAvro> sensorsState, SensorEventAvro event) {
        if (!sensorsState.containsKey(event.getId())) {
            return false;
        }

        SensorStateAvro oldState = sensorsState.get(event.getId());
        return oldState.getTimestamp().isAfter(event.getTimestamp())
                || oldState.getData().equals(event.getPayload());
    }
}
