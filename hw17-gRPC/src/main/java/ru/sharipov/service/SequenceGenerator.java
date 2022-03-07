package ru.sharipov.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.ClientRequest;
import ru.otus.protobuf.generated.SequenceGeneratorGrpc;
import ru.otus.protobuf.generated.SequenceResponse;
import ru.sharipov.utils.ThreadUtils;

import java.util.stream.LongStream;

public class SequenceGenerator extends SequenceGeneratorGrpc.SequenceGeneratorImplBase {
    @Override
    public void generate(ClientRequest request, StreamObserver<SequenceResponse> responseObserver) {
        var firstValue = request.getFirstValue();
        var lastValue = request.getLastValue();
        LongStream
                .range(firstValue + 1, lastValue)
                .forEach(value -> {
                    ThreadUtils.sleep(2000);
                    responseObserver.onNext(SequenceResponse.newBuilder().setValue(value).build());
                });
        responseObserver.onCompleted();
    }
}
