package ru.yandex.practicum.telemetry.processor;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.KafkaConfig;
import ru.yandex.practicum.telemetry.service.SnapshotService;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class SnapshotProcessor implements Runnable {

    private final Consumer<String, SpecificRecordBase> snapshotConsumer;
    private final KafkaConfig kafkaConfig;
    private final SnapshotService<SensorsSnapshotAvro> snapshotService;

    private static final Duration CLOSE_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration POLL_TIMEOUT = Duration.ofSeconds(5);
    private static final String SNAPSHOT_TOPIC_KEY = "snapshot";

    public SnapshotProcessor(
            @Qualifier("snapshotConsumer") Consumer<String, SpecificRecordBase> snapshotConsumer,
            KafkaConfig kafkaConfig,
            SnapshotService<SensorsSnapshotAvro> snapshotService
    ) {

        this.snapshotConsumer = snapshotConsumer;
        this.kafkaConfig = kafkaConfig;
        this.snapshotService = snapshotService;
    }

    @Override
    public void run() {
        try {
            snapshotConsumer.subscribe(List.of(kafkaConfig.getTopics().getProperty(SNAPSHOT_TOPIC_KEY)));

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = snapshotConsumer.poll(POLL_TIMEOUT);

                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    try {
                        SensorsSnapshotAvro snapshot = (SensorsSnapshotAvro) record.value();
                        snapshotService.processSnapshot(snapshot);
                    } catch (Exception e) {
                        log.error("Error while working with consumer: {}", e.getMessage());
                    }
                }
                snapshotConsumer.commitAsync();
            }

        } catch (Exception e) {
            log.error("Fatal error while working with consumer: {}", e.getMessage());
        } finally {
            snapshotConsumer.commitSync();
        }
    }

    @PreDestroy
    public void close() {
        snapshotConsumer.close(CLOSE_TIMEOUT);
    }
}
