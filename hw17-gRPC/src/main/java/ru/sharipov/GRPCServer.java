package ru.sharipov;


import io.grpc.ServerBuilder;
import ru.sharipov.service.SequenceGenerator;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var sequenceGenerator = new SequenceGenerator();

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(sequenceGenerator).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
