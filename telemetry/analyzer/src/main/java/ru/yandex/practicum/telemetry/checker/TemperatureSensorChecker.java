package ru.yandex.practicum.telemetry.checker;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.telemetry.checker.check.IntegerCheck;

@Component
public class TemperatureSensorChecker extends BaseChecker<TemperatureSensorAvro> {

    @Override
    public DeviceTypeAvro getDeviceType() {
        return DeviceTypeAvro.TEMPERATURE_SENSOR;
    }

    @Override
    protected TemperatureSensorAvro castToAvro(SpecificRecordBase data) {
        try {
            return (TemperatureSensorAvro) data;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Cannot cast data in " + getClass() + ": " + data);
        }
    }

    @Override
    protected IntegerCheck handleTemperature(TemperatureSensorAvro state) {
        return new IntegerCheck(state.getTemperatureC());
    }
}
