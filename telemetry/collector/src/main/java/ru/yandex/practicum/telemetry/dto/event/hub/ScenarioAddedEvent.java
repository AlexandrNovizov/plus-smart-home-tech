package ru.yandex.practicum.telemetry.dto.event.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.telemetry.dto.device.DeviceAction;
import ru.yandex.practicum.telemetry.dto.device.ScenarioCondition;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioAddedEvent extends HubEvent {

    @NotBlank
    @Length(min = 3, message = "name length should be > 3")
    private String name;
    @NotEmpty
    private List<ScenarioCondition> conditions;
    @NotEmpty
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
