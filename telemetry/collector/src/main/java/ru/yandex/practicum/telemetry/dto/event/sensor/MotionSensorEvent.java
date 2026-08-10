package ru.yandex.practicum.telemetry.dto.event.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MotionSensorEvent extends SensorEvent {

    @NotNull
    private Integer linkQuality;
    @NotNull
    private Boolean motion;
    @NotNull
    private Integer voltage;


    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
