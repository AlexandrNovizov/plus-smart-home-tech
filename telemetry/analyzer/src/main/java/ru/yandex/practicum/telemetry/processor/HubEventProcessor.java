package ru.yandex.practicum.telemetry.processor;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.KafkaConfig;
import ru.yandex.practicum.telemetry.service.HubEventService;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {

    private final Consumer<String, SpecificRecordBase> hubConsumer;
    private final KafkaConfig kafkaConfig;
    private final HubEventService hubService;

    private static final Duration CLOSE_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration POLL_TIMEOUT = Duration.ofSeconds(5);
    private static final String HUB_TOPIC_KEY = "hub.event";
    private final Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

    public HubEventProcessor(
            @Qualifier("hubConsumer") Consumer<String, SpecificRecordBase> hubConsumer,
            KafkaConfig kafkaConfig,
            HubEventService hubService
    ) {
        this.hubConsumer = hubConsumer;
        this.kafkaConfig = kafkaConfig;
        this.hubService = hubService;
    }

    @Override
    public void run() {
        try {
            log.trace("Subscribing topics");
            hubConsumer.subscribe(List.of(kafkaConfig.getTopics().getProperty(HUB_TOPIC_KEY)));

            while (true) {
                log.trace("Polling records");
                ConsumerRecords<String, SpecificRecordBase> records = hubConsumer.poll(POLL_TIMEOUT);
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    HubEventAvro event = (HubEventAvro) record.value();
                    hubService.processEvent(event);
                    offsets.put(
                            new TopicPartition(record.topic(), record.partition()),
                            new OffsetAndMetadata(record.offset() + 1, "")
                    );
                }
                if (!records.isEmpty()) {
                    hubConsumer.commitAsync(
                        offsets,
                        (commitedOffsets, exception) -> {
                            if (exception != null) {
                                log.warn("Error while commiting messages. Offsets: {}", commitedOffsets, exception);
                            }
                        });
                }
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Fatal error while working with consumer: {}", e.getMessage());
        } finally {
            hubConsumer.commitSync(offsets);
            hubConsumer.close(CLOSE_TIMEOUT);
        }
    }

    @PreDestroy
    public void close() {
        hubConsumer.wakeup();
    }
}
