package org.example;

import io.grpc.stub.StreamObserver;
import ru.karaban.grpc.TelemetryProto;
import ru.karaban.grpc.TelemetryServiceGrpc;

public class TelemetryService extends TelemetryServiceGrpc.TelemetryServiceImplBase {
    private final float DEFAULT_TEMPERATURE = 25.5f;
    private final float DEFAULT_PRESSURE = 650.0f;
    private final float DEFAULT_HUMIDITY = 85.5f;
    @Override
    public void getTelemetry(TelemetryProto.TelemetryRequest request,
                             StreamObserver<TelemetryProto.Telemetry> responseObserver) {

        if(request.getPlace().equals("default")) {

            TelemetryProto.Telemetry response
                    = TelemetryProto.Telemetry.newBuilder()
                    .setTemperature(DEFAULT_TEMPERATURE)
                    .setPressure(DEFAULT_PRESSURE)
                    .setHumidity(DEFAULT_HUMIDITY)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
