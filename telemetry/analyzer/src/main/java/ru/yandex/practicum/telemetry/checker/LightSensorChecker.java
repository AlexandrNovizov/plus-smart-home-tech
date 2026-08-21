package ru.yandex.practicum.telemetry.checker;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.telemetry.checker.check.IntegerCheck;

@Component
public class LightSensorChecker extends BaseChecker<LightSensorAvro> {

    @Override
    public DeviceTypeAvro getDeviceType() {
        return DeviceTypeAvro.LIGHT_SENSOR;
    }

    @Override
    protected LightSensorAvro castToAvro(SpecificRecordBase data) {
        try {
            return (LightSensorAvro) data;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Cannot cast data in " + getClass() + ": " + data);
        }
    }

    @Override
    protected IntegerCheck handleLuminosity(LightSensorAvro state) {
        return new IntegerCheck(state.getLuminosity());
    }
}
