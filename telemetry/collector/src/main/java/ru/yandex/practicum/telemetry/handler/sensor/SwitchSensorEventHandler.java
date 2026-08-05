package ru.yandex.practicum.telemetry.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.dto.event.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.dto.event.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.dto.event.sensor.SwitchSensorEvent;
import ru.yandex.practicum.telemetry.kafka.KafkaEventProducer;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {

    public SwitchSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEvent event) {
        if (!(event instanceof SwitchSensorEvent)) {
            throw new IllegalArgumentException(String.format("Can't map %s to SwitchSensorAvro", event.getType().name()));
        }

        SwitchSensorEvent switchEvent = (SwitchSensorEvent) event;

        return SwitchSensorAvro.newBuilder()
                .setState(switchEvent.getState())
                .build();
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
