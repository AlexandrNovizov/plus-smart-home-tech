package ru.yandex.practicum.telemetry.checker.check;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.telemetry.model.Condition;

@Getter
@RequiredArgsConstructor
public abstract class BaseCheck<T> {

    private final T value;

    public boolean check(Condition condition) {
        switch (condition.getOperation()) {
            case GREATER_THAN:
                return handleGreaterThan(condition);
            case LOWER_THAN:
                return handleLowerThan(condition);
            case EQUALS:
                return handleEquals(condition);
            case null, default:
                throw new IllegalArgumentException("Can't find handler for operation: " +
                        (condition.getOperation() == null ? null : condition.getOperation().name())
                );
        }
    }

    protected boolean handleEquals(Condition condition) {
        uoe(condition);
        return false;
    }
    protected boolean handleLowerThan(Condition condition) {
        uoe(condition);
        return false;
    }
    protected boolean handleGreaterThan(Condition condition) {
        uoe(condition);
        return false;
    }

    private void uoe(Condition condition) {
        throw new UnsupportedOperationException(
                String.format("Unsupported operation %s for device type %s",
                        condition.getOperation().name(), condition.getType())
        );
    }
}
