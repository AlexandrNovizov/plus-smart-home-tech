package ru.yandex.practicum.telemetry.checker;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.checker.check.BaseCheck;
import ru.yandex.practicum.telemetry.checker.check.BooleanCheck;
import ru.yandex.practicum.telemetry.checker.check.IntegerCheck;
import ru.yandex.practicum.telemetry.model.Condition;
import ru.yandex.practicum.telemetry.model.ConditionType;

public abstract class BaseChecker<T extends SpecificRecordBase> implements Checker {
    @Override
    public boolean checkCondition(Object state, Condition condition) {
        BaseCheck<?> check;
        T specificState = castToAvro(state);
        switch (condition.getType()) {
            case MOTION:
                 check = handleMotion(specificState);
                 break;
            case TEMPERATURE:
                check = handleTemperature(specificState);
                break;
            case LUMINOSITY:
                check = handleLuminosity(specificState);
                break;
            case SWITCH:
                check = handleSwitch(specificState);
                break;
            case HUMIDITY:
                check = handleHumidity(specificState);
                break;
            case CO2LEVEL:
                check = handleCo2Level(specificState);
                break;
            case null, default:
                throw new IllegalArgumentException("Can't find handler for type: " +
                    (condition.getType() == null ? null : condition.getType().name()));
        }

        return check.check(condition);
    }

    abstract protected T castToAvro(Object data);

    @Override
    public abstract DeviceTypeAvro getDeviceType();

    protected BooleanCheck handleMotion(T state) {
        uoe(ConditionType.MOTION);
        return null;
    }
    protected IntegerCheck handleTemperature(T state) {
        uoe(ConditionType.TEMPERATURE);
        return null;
    }
    protected IntegerCheck handleLuminosity(T state) {
        uoe(ConditionType.LUMINOSITY);
        return null;
    }
    protected BooleanCheck handleSwitch(T state) {
        uoe(ConditionType.SWITCH);
        return null;
    }
    protected IntegerCheck handleHumidity(T state) {
        uoe(ConditionType.HUMIDITY);
        return null;
    }
    protected IntegerCheck handleCo2Level(T state) {
        uoe(ConditionType.CO2LEVEL);
        return null;
    }

    private void uoe(ConditionType conditionType) {
        throw new UnsupportedOperationException(
                String.format("Unsupported condition type %s for device type %s",
                        conditionType.name(), getDeviceType())
        );
    }
}
