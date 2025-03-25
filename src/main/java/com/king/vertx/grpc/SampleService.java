package com.king.vertx.grpc;

import com.king.vertx.Request;
import com.king.vertx.Response;
import com.king.vertx.SampleGrpc;
import io.grpc.stub.StreamObserver;
import io.vertx.core.Vertx;

import static com.king.vertx.grpc.ContextLocalVertxServiceProvider.CONTEXT_LOCAL;

public class SampleService extends SampleGrpc.SampleImplBase {

    @Override
    public void unary(Request request, StreamObserver<Response> responseObserver) {
        System.out.println("GRPC: unary request");

        var currentContext = CONTEXT_LOCAL.get(Vertx.currentContext());
        if (currentContext != null) {
            responseObserver.onNext(Response.newBuilder().setValue(currentContext.get(String.class).orElseThrow()).build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("unable to find current context"));
        }
    }

}
