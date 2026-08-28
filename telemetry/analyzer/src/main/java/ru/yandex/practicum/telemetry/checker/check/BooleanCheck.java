package ru.yandex.practicum.telemetry.checker.check;

import ru.yandex.practicum.telemetry.model.Condition;

public class BooleanCheck extends BaseCheck<Boolean> {

    public BooleanCheck(Boolean value) {
        super(value);
    }

    @Override
    protected boolean handleEquals(Condition condition) {
        return getValue() == (condition.getValue() != 0);
    }
}
