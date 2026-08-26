package ru.yandex.practicum.telemetry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.service.SnapshotService;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private final Producer<String, SpecificRecordBase> producer;
    private final Consumer<String, SpecificRecordBase> consumer;
    private final SnapshotService snapshotService;
    private final KafkaConfig kafkaConfig;

    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final Duration POLL_TIMEOUT = Duration.ofSeconds(5);
    private static final String SENSOR_EVENT_TOPIC_KEY = "sensor.event";
    private static final String SNAPSHOT_EVENT_TOPIC_KEY = "snapshot";
    private final Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

    public void start() {
        try {
            consumer.subscribe(List.of(kafkaConfig.getTopics().getProperty(SENSOR_EVENT_TOPIC_KEY)));
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(POLL_TIMEOUT);
                Future<RecordMetadata> send = null;
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    SensorEventAvro sensorEvent = (SensorEventAvro) record.value();
                    Optional<SensorsSnapshotAvro> optSnapshot = snapshotService.updateState(sensorEvent);
                    offsets.put(
                            new TopicPartition(record.topic(), record.partition()),
                            new OffsetAndMetadata(record.offset() + 1, "")
                    );


                    if (optSnapshot.isPresent()) {
                        SensorsSnapshotAvro snapshot = optSnapshot.get();
                        ProducerRecord<String, SpecificRecordBase> snapshotRecord = new ProducerRecord<>(
                                kafkaConfig.getTopics().getProperty(SNAPSHOT_EVENT_TOPIC_KEY),
                                null,
                                Instant.now().toEpochMilli(),
                                snapshot.getHubId(),
                                snapshot
                        );
                        send = producer.send(snapshotRecord);
                    }
                }
                if (!records.isEmpty() && send != null && send.isDone()) {
                    consumer.commitAsync(
                        offsets,
                        (commitedOffsets, exception) -> {
                            if (exception != null) {
                                log.warn("Error while commiting messages. Offsets: {}", commitedOffsets, exception);
                            }
                        }
                    );
                }
            }
        } catch (WakeupException ignored) {
            try {
                log.info("Commiting offsets");
                consumer.commitSync(offsets, TIMEOUT);
            } finally {
                log.info("Closing consumer");
                consumer.close(TIMEOUT);
            }
        } catch (Exception e) {
            log.error("Error while handling events from sensors: ", e);
        } finally {
            try {
                log.info("Flushing data");
                producer.flush();
            } finally {
                log.info("Closing producer");
                producer.close(TIMEOUT);
            }
        }
    }

}
