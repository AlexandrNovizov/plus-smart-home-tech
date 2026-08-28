package ru.yandex.practicum.telemetry.service;

import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.checker.Checker;
import ru.yandex.practicum.telemetry.mapper.ActionToProtoMapper;
import ru.yandex.practicum.telemetry.model.Action;
import ru.yandex.practicum.telemetry.model.Scenario;
import ru.yandex.practicum.telemetry.repository.ScenarioRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class SensorSnapshotService implements SnapshotService<SensorsSnapshotAvro> {

    private final ScenarioRepository scenarioRepository;
    private final Map<DeviceTypeAvro, Checker> checkers;
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public SensorSnapshotService(
            ScenarioRepository scenarioRepository,
            List<Checker> checkers,
            @GrpcClient("hub-router") HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient
    ) {
        this.scenarioRepository = scenarioRepository;
        this.checkers = checkers.stream()
                .collect(
                        Collectors.toMap(Checker::getDeviceType, Function.identity())
                );
        this.hubRouterClient = hubRouterClient;
    }

    @Override
    public void processSnapshot(SensorsSnapshotAvro snapshot) {
        List<Scenario> scenarios = scenarioRepository.findByHubId(snapshot.getHubId());

        List<Long> passedIds = scenarios.stream()
                .filter(scenario -> processConditions(scenario, snapshot))
                .map(Scenario::getId)
                .toList();

        List<Scenario> byIdsInAndActions = scenarioRepository.findByIdIn(passedIds);

        byIdsInAndActions
                .forEach(this::processActions);
    }

    private boolean processConditions(Scenario scenario, SensorsSnapshotAvro snapshot) {
        for (String sensorId : scenario.getConditions().keySet()) {
            SensorStateAvro sensorState = snapshot.getSensorsState().get(sensorId);
            if (sensorState == null) {
                log.info("State not found for device with id: {}", sensorId);
                return false;
            }

            boolean checkResult = checkers.get(deduceType(sensorState.getData()))
                    .checkCondition(sensorState.getData(), scenario.getConditions().get(sensorId));

            if (!checkResult) {
                return false;
            }
        }
        return true;
    }

    private void processActions(Scenario scenario) {
        for (String sensorId : scenario.getActions().keySet()) {
            Action action = scenario.getActions().get(sensorId);
            DeviceActionProto deviceActionProto = DeviceActionProto.newBuilder()
                    .setSensorId(sensorId)
                    .setType(ActionToProtoMapper.toProto(action.getType()))
                    .setValue(action.getValue())
                    .build();

            Instant now = Instant.now();
            DeviceActionRequest request = DeviceActionRequest.newBuilder()
                    .setHubId(scenario.getHubId())
                    .setAction(deviceActionProto)
                    .setScenarioName(scenario.getName())
                    .setTimestamp(Timestamp.newBuilder()
                                    .setSeconds(now.getEpochSecond())
                                    .setNanos(now.getNano())
                                    .build()
                    )
                    .build();

            try {
                hubRouterClient.handleDeviceAction(request);
            } catch (Exception e) {
                throw new RuntimeException("Error while sending gRPC action: " + e.getMessage());
            }
        }
    }

    private DeviceTypeAvro deduceType(Object data) {
        if (data instanceof ClimateSensorAvro) {
            return DeviceTypeAvro.CLIMATE_SENSOR;
        }
        if (data instanceof LightSensorAvro) {
            return DeviceTypeAvro.LIGHT_SENSOR;
        }
        if (data instanceof MotionSensorAvro) {
            return DeviceTypeAvro.MOTION_SENSOR;
        }
        if (data instanceof SwitchSensorAvro) {
            return DeviceTypeAvro.SWITCH_SENSOR;
        }
        if (data instanceof TemperatureSensorAvro) {
            return DeviceTypeAvro.TEMPERATURE_SENSOR;
        }
        throw new IllegalArgumentException("Can't find DeviceTypeAvro enum value for " + data);
    }
}
