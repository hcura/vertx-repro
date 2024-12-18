package com.king.vertx.grpc;

import com.king.vertx.Request;
import com.king.vertx.Response;
import com.king.vertx.SampleGrpc;
import io.grpc.stub.StreamObserver;

public class SampleService extends SampleGrpc.SampleImplBase {

    @Override
    public void unary(Request request, StreamObserver<Response> responseObserver) {
        System.out.println("unary");
        var response = Response.newBuilder().setValue(request.getValue()).build();

        responseObserver.onNext(response);
    }

}
