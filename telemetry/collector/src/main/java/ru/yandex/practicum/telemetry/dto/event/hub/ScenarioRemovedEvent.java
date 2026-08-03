package ru.yandex.practicum.telemetry.dto.event.hub;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioRemovedEvent extends HubEvent {

    @NotBlank
    @Min(value = 3, message = "name length should be > 3")
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
