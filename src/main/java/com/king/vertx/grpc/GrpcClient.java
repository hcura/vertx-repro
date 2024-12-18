package com.king.vertx.grpc;

import com.king.vertx.Request;
import com.king.vertx.SampleGrpc;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.StatusRuntimeException;

public class GrpcClient {
    public static void main(String[] args) {
        new GrpcClient().start();
    }

    private void start() {
        var channel = Grpc.newChannelBuilder("localhost:8081", InsecureChannelCredentials.create()).build();

        var stub = SampleGrpc.newBlockingStub(channel);

        try {
            var response = stub.unary(Request.newBuilder().setValue("test").build());
            System.out.println(response.getValue());
        } catch (StatusRuntimeException sre) {
            System.out.println(sre.getMessage());
        }
    }
}
