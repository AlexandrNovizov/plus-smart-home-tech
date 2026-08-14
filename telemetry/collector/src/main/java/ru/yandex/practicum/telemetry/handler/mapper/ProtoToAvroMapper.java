package ru.yandex.practicum.telemetry.handler.mapper;

import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

public class ProtoToAvroMapper {

    public static DeviceTypeAvro toAvro(DeviceTypeProto proto) {
        switch (proto) {
            case TEMPERATURE_SENSOR:
                return DeviceTypeAvro.TEMPERATURE_SENSOR;
            case CLIMATE_SENSOR:
                return DeviceTypeAvro.CLIMATE_SENSOR;
            case SWITCH_SENSOR:
                return DeviceTypeAvro.SWITCH_SENSOR;
            case MOTION_SENSOR:
                return DeviceTypeAvro.MOTION_SENSOR;
            case LIGHT_SENSOR:
                return DeviceTypeAvro.LIGHT_SENSOR;
            case UNRECOGNIZED:
            case null, default:
                throw new IllegalArgumentException("Can't find DeviceTypeAvro mapper for " + (proto == null ? null : proto.name()));
        }
    }

    public static ConditionOperationAvro toAvro(ConditionOperationProto proto) {
        switch (proto) {
            case EQUALS:
                return ConditionOperationAvro.EQUALS;
            case GREATER_THAN:
                return ConditionOperationAvro.GREATER_THAN;
            case LOWER_THAN:
                return ConditionOperationAvro.LOWER_THAN;
            case UNRECOGNIZED:
            case null, default:
                throw new IllegalArgumentException("Can't find ConditionOperationAvro mapper for " + (proto == null ? null : proto.name()));
        }
    }

    public static ConditionTypeAvro toAvro(ConditionTypeProto proto) {
        switch (proto) {
            case HUMIDITY:
                return ConditionTypeAvro.HUMIDITY;
            case SWITCH:
                return ConditionTypeAvro.SWITCH;
            case LUMINOSITY:
                return ConditionTypeAvro.LUMINOSITY;
            case TEMPERATURE:
                return ConditionTypeAvro.TEMPERATURE;
            case MOTION:
                return ConditionTypeAvro.MOTION;
            case CO2LEVEL:
                return ConditionTypeAvro.CO2LEVEL;
            case UNRECOGNIZED:
            case null, default:
                throw new IllegalArgumentException("Can't find ConditionTypeAvro mapper for " + (proto == null ? null : proto.name()));
        }
    }

    public static ScenarioConditionAvro toAvro(ScenarioConditionProto proto) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(proto.getSensorId())
                .setOperation(toAvro(proto.getOperation()))
                .setType(toAvro(proto.getType()))
                .setValue(valueToAvro(proto))
                .build();
    }

    public static ActionTypeAvro toAvro(ActionTypeProto proto) {
        switch (proto) {
            case INVERSE:
                return ActionTypeAvro.INVERSE;
            case DEACTIVATE:
                return ActionTypeAvro.DEACTIVATE;
            case ACTIVATE:
                return ActionTypeAvro.ACTIVATE;
            case SET_VALUE:
                return ActionTypeAvro.SET_VALUE;
            case UNRECOGNIZED:
            case null, default:
                throw new IllegalArgumentException("Can't find ActionTypeAvro mapper for " + (proto == null ? null : proto.name()));
        }
    }

    public static DeviceActionAvro toAvro(DeviceActionProto proto) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(proto.getSensorId())
                .setType(toAvro(proto.getType()))
                .setValue(proto.getValue())
                .build();
    }

    private static Object valueToAvro(ScenarioConditionProto proto) {
        switch (proto.getValueCase()) {
            case INT_VALUE:
                return proto.getIntValue();
            case BOOL_VALUE:
                return proto.getBoolValue();
            case VALUE_NOT_SET:
            case null:
                return null;
            default:
                throw new IllegalArgumentException("Can't find value mapper for " + proto.getValueCase().name());
        }
    }
}
