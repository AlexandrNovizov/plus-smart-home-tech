package ru.yandex.practicum.telemetry.dto.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceAction {

    @NotBlank
    private String sensorId;
    @NotNull
    private DeviceActionType type;
    private Integer value;
}
