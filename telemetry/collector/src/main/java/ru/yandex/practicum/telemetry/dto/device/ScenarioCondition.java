package ru.yandex.practicum.telemetry.dto.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioCondition {

    @NotBlank
    private String sensorId;
    @NotNull
    private ScenarioConditionType type;
    @NotNull
    private OperationType operation;
    @NotNull
    private Object value;
}
