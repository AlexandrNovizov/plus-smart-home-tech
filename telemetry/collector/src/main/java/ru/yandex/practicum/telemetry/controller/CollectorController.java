package ru.yandex.practicum.telemetry.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.handler.hub.HubEventHandler;
import ru.yandex.practicum.telemetry.handler.sensor.SensorEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
public class CollectorController extends CollectorControllerGrpc.CollectorControllerImplBase {


    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorHandlers;
    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubHandlers;

    public CollectorController(List<SensorEventHandler> sensorEventHandlerList, List<HubEventHandler> hubEventHandlerList) {
        sensorHandlers = sensorEventHandlerList.stream()
                .collect(Collectors.toMap(SensorEventHandler::getType, Function.identity()));

        hubHandlers = hubEventHandlerList.stream()
                .collect(Collectors.toMap(HubEventHandler::getType, Function.identity()));
    }

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            if (!sensorHandlers.containsKey(request.getPayloadCase())) {
                throw new IllegalArgumentException("Can't find handler for event " + request.getPayloadCase().name());
            }

            sensorHandlers.get(request.getPayloadCase()).handle(request);

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            if (!hubHandlers.containsKey(request.getPayloadCase())) {
                throw new IllegalArgumentException("Can't find handler for event " + request.getPayloadCase().name());
            }

            hubHandlers.get(request.getPayloadCase()).handle(request);

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}
