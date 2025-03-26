package com.king.vertx.grpc;

import com.king.vertx.Request;
import com.king.vertx.Response;
import com.king.vertx.SampleGrpc;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;
import io.vertx.core.Vertx;

import static com.king.vertx.grpc.ContextLocalVertxServiceProvider.CUSTOM_CONTEXT_LOCAL;
import static com.king.vertx.grpc.InterceptorWithGrpcContext.GRPC_CONTEXT_KEY;

public class GrpcContextService extends SampleGrpc.SampleImplBase {

    @Override
    public void unary(Request request, StreamObserver<Response> responseObserver) {
        System.out.println("GRPC: unary request");

        var currentContext = GRPC_CONTEXT_KEY.get();
        if (currentContext != null) {
            responseObserver.onNext(Response.newBuilder().setValue(currentContext).build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("unable to find current context"));
        }
    }

}
