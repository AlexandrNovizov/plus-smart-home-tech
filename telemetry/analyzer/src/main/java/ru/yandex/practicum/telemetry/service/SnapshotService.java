package ru.yandex.practicum.telemetry.service;

import org.apache.avro.specific.SpecificRecordBase;

public interface SnapshotService<T extends SpecificRecordBase> {

    void processSnapshot(T snapshot);
}
