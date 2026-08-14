package ru.yandex.practicum.telemetry.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.kafka.KafkaEventProducer;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {

    public SwitchSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEventProto event) {

        SwitchSensorProto switchEvent = event.getSwitchSensor();

        return SwitchSensorAvro.newBuilder()
                .setState(switchEvent.getState())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }
}
