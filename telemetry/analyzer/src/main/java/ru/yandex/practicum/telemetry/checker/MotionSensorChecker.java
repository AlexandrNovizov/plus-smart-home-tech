package ru.yandex.practicum.telemetry.checker;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.telemetry.checker.check.BooleanCheck;

@Component
public class MotionSensorChecker extends BaseChecker<MotionSensorAvro> {

    @Override
    public DeviceTypeAvro getDeviceType() {
        return DeviceTypeAvro.MOTION_SENSOR;
    }

    @Override
    protected MotionSensorAvro castToAvro(SpecificRecordBase data) {
        try {
            return (MotionSensorAvro) data;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Cannot cast data in " + getClass() + ": " + data);
        }
    }

    @Override
    protected BooleanCheck handleMotion(MotionSensorAvro state) {
        return new BooleanCheck(state.getMotion());
    }
}
