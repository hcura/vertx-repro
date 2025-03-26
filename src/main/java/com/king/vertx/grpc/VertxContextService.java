package com.king.vertx.grpc;

import com.king.vertx.Request;
import com.king.vertx.Response;
import com.king.vertx.SampleGrpc;
import io.grpc.stub.StreamObserver;
import io.vertx.core.Vertx;

import static com.king.vertx.grpc.ContextLocalVertxServiceProvider.CUSTOM_CONTEXT_LOCAL;

public class VertxContextService extends SampleGrpc.SampleImplBase {

    @Override
    public void unary(Request request, StreamObserver<Response> responseObserver) {
        System.out.println("GRPC: unary request");

        var currentContext = Vertx.currentContext().getLocal(CUSTOM_CONTEXT_LOCAL);
        if (currentContext != null) {
            responseObserver.onNext(Response.newBuilder().setValue(currentContext.get(String.class).orElseThrow()).build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("unable to find current context"));
        }
    }

}
