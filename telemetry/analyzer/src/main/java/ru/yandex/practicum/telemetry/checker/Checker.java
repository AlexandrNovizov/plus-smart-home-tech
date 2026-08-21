package ru.yandex.practicum.telemetry.checker;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.model.Condition;

public interface Checker {

    boolean checkCondition(SpecificRecordBase state, Condition condition);

    DeviceTypeAvro getDeviceType();
}
