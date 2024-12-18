package com.king.vertx.grpc;

import com.king.vertx.Request;
import com.king.vertx.SampleGrpc;
import io.vertx.core.Vertx;
import io.vertx.core.net.SocketAddress;
import io.vertx.grpc.common.GrpcReadStream;
import io.vertx.grpcio.client.GrpcIoClient;

public class GrpcClient {
    public static void main(String[] args) {
        new GrpcClient().start();
    }

    private void start() {
        var vertx = Vertx.vertx();
        var client = GrpcIoClient.client(vertx);

        var server = SocketAddress.inetSocketAddress(8081, "localhost");

        client.request(server, SampleGrpc.getUnaryMethod())
                .compose(request -> {
                    request.end(Request
                            .newBuilder()
                            .setValue("Test")
                            .build());
                    return request.response().compose(GrpcReadStream::last);
                })
                .onSuccess(reply -> System.out.println("Received " + reply.getValue()))
                .await();
    }
}
