package ru.yandex.practicum.telemetry.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.telemetry.dto.event.hub.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.dto.event.hub.HubEventType;
import ru.yandex.practicum.telemetry.kafka.KafkaEventProducer;

@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {

    public DeviceRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public DeviceRemovedEventAvro mapToAvro(HubEvent event) {
        if (!(event instanceof DeviceRemovedEvent)) {
            throw new IllegalArgumentException(String.format("Can't map %s to DeviceRemovedEventAvro", event.getType().name()));
        }

        DeviceRemovedEvent deviceRemovedEvent  = (DeviceRemovedEvent) event;

        return DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedEvent.getId())
                .build();
    }

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
