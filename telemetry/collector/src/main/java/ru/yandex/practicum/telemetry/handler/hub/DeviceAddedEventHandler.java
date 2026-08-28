package ru.yandex.practicum.telemetry.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.telemetry.handler.mapper.ProtoToAvroMapper;
import ru.yandex.practicum.telemetry.kafka.KafkaEventProducer;

@Component
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {

    public DeviceAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEventProto event) {

        DeviceAddedEventProto deviceAdded = event.getDeviceAdded();

        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAdded.getId())
                .setType(ProtoToAvroMapper.toAvro(deviceAdded.getType()))
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }
}
