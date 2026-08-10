package ru.yandex.practicum.telemetry.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.telemetry.dto.event.sensor.ClimateSensorEvent;
import ru.yandex.practicum.telemetry.dto.event.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.dto.event.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.kafka.KafkaEventProducer;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> {

    public ClimateSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEvent event) {
        if (!(event instanceof ClimateSensorEvent)) {
            throw new IllegalArgumentException(String.format("Can't map %s to ClimateSensorAvro", event.getType().name()));
        }

        ClimateSensorEvent climateEvent = (ClimateSensorEvent) event;

        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(climateEvent.getTemperatureC())
                .setCo2Level(climateEvent.getCo2Level())
                .setHumidity(climateEvent.getHumidity())
                .build();
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
