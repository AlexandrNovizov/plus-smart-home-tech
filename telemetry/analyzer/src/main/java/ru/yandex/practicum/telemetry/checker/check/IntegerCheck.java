package ru.yandex.practicum.telemetry.checker.check;

import ru.yandex.practicum.telemetry.model.Condition;

import java.util.Objects;

public class IntegerCheck extends BaseCheck<Integer> {

    public IntegerCheck(Integer value) {
        super(value);
    }

    @Override
    protected boolean handleEquals(Condition condition) {
        return Objects.equals(getValue(), condition.getValue());
    }

    @Override
    protected boolean handleLowerThan(Condition condition) {
        return getValue() < condition.getValue();
    }

    @Override
    protected boolean handleGreaterThan(Condition condition) {
        return getValue() > condition.getValue();
    }
}
