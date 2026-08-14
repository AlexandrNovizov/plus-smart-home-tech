package ru.yandex.practicum.telemetry.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.handler.mapper.ProtoToAvroMapper;
import ru.yandex.practicum.telemetry.kafka.KafkaEventProducer;

import java.util.List;

@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public ScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEventProto event) {

        ScenarioAddedEventProto scenarioAdded = event.getScenarioAdded();

        List<ScenarioConditionAvro> conditions = scenarioAdded.getConditionsList().stream()
                .map(ProtoToAvroMapper::toAvro)
                .toList();

        List<DeviceActionAvro> actions = scenarioAdded.getActionsList().stream()
                .map(ProtoToAvroMapper::toAvro)
                .toList();


        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAdded.getName())
                .setConditions(conditions)
                .setActions(actions)
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }
}
