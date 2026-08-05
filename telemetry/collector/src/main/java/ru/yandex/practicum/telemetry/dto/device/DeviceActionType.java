package ru.yandex.practicum.telemetry.dto.device;

import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;

public enum DeviceActionType {
    ACTIVATE, DEACTIVATE, INVERSE, SET_VALUE;

    public ActionTypeAvro mapToAvro() {
        switch (this) {
            case ACTIVATE:
                return ActionTypeAvro.ACTIVATE;
            case DEACTIVATE:
                return ActionTypeAvro.DEACTIVATE;
            case INVERSE:
                return ActionTypeAvro.INVERSE;
            case SET_VALUE:
                return ActionTypeAvro.SET_VALUE;
            case null:
                throw new IllegalArgumentException("Can't map null to DeviceActionAvro");
            default:
                throw new IllegalArgumentException(String.format("Can't map DeviceAction::%s to DeviceActionAvro", this));
        }
    }
}
