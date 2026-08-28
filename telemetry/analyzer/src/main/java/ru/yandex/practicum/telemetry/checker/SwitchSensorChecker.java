package ru.yandex.practicum.telemetry.checker;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.checker.check.BooleanCheck;

@Component
public class SwitchSensorChecker extends BaseChecker<SwitchSensorAvro> {

    @Override
    public DeviceTypeAvro getDeviceType() {
        return DeviceTypeAvro.SWITCH_SENSOR;
    }

    @Override
    protected SwitchSensorAvro castToAvro(Object data) {
        try {
            return (SwitchSensorAvro) data;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Cannot cast data in " + getClass() + ": " + data);
        }
    }

    @Override
    protected BooleanCheck handleSwitch(SwitchSensorAvro state) {
        return new BooleanCheck(state.getState());
    }
}
