package ru.yandex.practicum.telemetry.handler.hub;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.kafka.KafkaEventProducer;

@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {

    private final KafkaEventProducer producer;

    public abstract T mapToAvro(HubEvent event);

    @Override
    public void handle(HubEvent event) {
        if (!event.getType().equals(getType())) {
            throw new IllegalArgumentException("Unknown event type: " + event.getType());
        }

        T payload = mapToAvro(event);

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();

        producer.send(hubEventAvro);
    }
}
