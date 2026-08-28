package ru.yandex.practicum.telemetry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@Getter
@AllArgsConstructor
@ConfigurationProperties("kafka")
public class KafkaConfig {

    private Properties hub;
    private Properties snapshots;
    private Properties topics;
}
