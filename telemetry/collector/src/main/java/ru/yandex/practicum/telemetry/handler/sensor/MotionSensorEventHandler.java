package ru.yandex.practicum.telemetry.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.telemetry.dto.event.sensor.MotionSensorEvent;
import ru.yandex.practicum.telemetry.dto.event.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.dto.event.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.kafka.KafkaEventProducer;

@Component
public class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> {

    public MotionSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected MotionSensorAvro mapToAvro(SensorEvent event) {
        if (!(event instanceof MotionSensorEvent)) {
            throw new IllegalArgumentException(String.format("Can't map %s to MotionSensorAvro", event.getType().name()));
        }

        MotionSensorEvent motionEvent = (MotionSensorEvent) event;

        return MotionSensorAvro.newBuilder()
                .setLinkQuality(motionEvent.getLinkQuality())
                .setMotion(motionEvent.getMotion())
                .setVoltage(motionEvent.getVoltage())
                .build();
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
