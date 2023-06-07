package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {

        Server server = ServerBuilder.forPort(8080)
                .addService(new ImageService())
                .addService(new TelemetryService())
                .build();

        server.start();

        System.out.println("Server started");

        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
