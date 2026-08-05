package ru.yandex.practicum.telemetry.dto.event.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioRemovedEvent extends HubEvent {

    @NotBlank
    @Length(min = 3, message = "name length should be > 3")
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
