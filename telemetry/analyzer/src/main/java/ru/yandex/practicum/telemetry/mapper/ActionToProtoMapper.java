package ru.yandex.practicum.telemetry.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.telemetry.model.ActionType;

@UtilityClass
public class ActionToProtoMapper {

    public static ActionTypeProto toProto(ActionType action) {
        switch (action) {
            case ACTIVATE:
                return ActionTypeProto.ACTIVATE;
            case DEACTIVATE:
                return ActionTypeProto.DEACTIVATE;
            case INVERSE:
                return ActionTypeProto.INVERSE;
            case SET_VALUE:
                return ActionTypeProto.SET_VALUE;
            case null, default:
                throw new IllegalArgumentException("Can't find ActionTypeProto mapper for " +
                        (action == null ? null : action.name()));
        }
    }
}
