package ru.yandex.practicum.telemetry.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.telemetry.dto.event.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.dto.event.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.dto.event.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.telemetry.kafka.KafkaEventProducer;

@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {

    public TemperatureSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEvent event) {
        if (!(event instanceof TemperatureSensorEvent)) {
            throw new IllegalArgumentException(String.format("Can't map %s to TemperatureSensorAvro", event.getType().name()));
        }

        TemperatureSensorEvent temperatureEvent = (TemperatureSensorEvent) event;

        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(temperatureEvent.getTemperatureC())
                .setTemperatureF(temperatureEvent.getTemperatureF())
                .build();
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
