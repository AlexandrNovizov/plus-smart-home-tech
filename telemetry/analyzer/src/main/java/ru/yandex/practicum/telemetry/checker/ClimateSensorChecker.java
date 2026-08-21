package ru.yandex.practicum.telemetry.checker;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.checker.check.IntegerCheck;

@Component
public class ClimateSensorChecker extends BaseChecker<ClimateSensorAvro> {

    @Override
    public DeviceTypeAvro getDeviceType() {
        return DeviceTypeAvro.CLIMATE_SENSOR;
    }

    @Override
    protected ClimateSensorAvro castToAvro(SpecificRecordBase data) {
        try {
            return (ClimateSensorAvro) data;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Cannot cast data in " + getClass() + ": " + data);
        }
    }

    @Override
    protected IntegerCheck handleTemperature(ClimateSensorAvro state) {
        return new IntegerCheck(state.getTemperatureC());
    }

    @Override
    protected IntegerCheck handleHumidity(ClimateSensorAvro state) {
        return new IntegerCheck(state.getHumidity());
    }

    @Override
    protected IntegerCheck handleCo2Level(ClimateSensorAvro state) {
        return new IntegerCheck(state.getCo2Level());
    }
}
