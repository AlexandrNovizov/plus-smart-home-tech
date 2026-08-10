package ru.yandex.practicum.telemetry.dto.event.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SwitchSensorEvent extends SensorEvent {

    @NotNull
    private Boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
