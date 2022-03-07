package ru.sharipov;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.*;
import ru.sharipov.utils.ThreadUtils;

import java.util.Arrays;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var stub = SequenceGeneratorGrpc.newStub(channel);
        long[] response = new long[1];
        response[0] = -1;
        stub.generate(ClientRequest.newBuilder().setFirstValue(0).setLastValue(30).build(), new StreamObserver<>() {
            @Override
            public void onNext(SequenceResponse value) {
                System.out.println("Число от сервера:" + value.getValue());
                response[0] = value.getValue();
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Произошла ошибка:" + Arrays.toString(t.getStackTrace()));
            }

            @Override
            public void onCompleted() {
                System.out.println("Получены все значения от сервера");
            }
        });
        var currentValue = 0;
        for (int i = 0; i <= 50; i++) {
            ThreadUtils.sleep(1000);
            currentValue++;
            if (response[0] != -1) {
                currentValue += response[0];
                response[0] = -1;
            }
            System.out.println("currentValue:" + currentValue);
        }
        channel.shutdown();
    }
}
