package com.king.vertx.grpc;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.vertx.core.Vertx;

import static com.king.vertx.grpc.ContextLocalVertxServiceProvider.CONTEXT_LOCAL;

public class SampleInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        var currentContext = CONTEXT_LOCAL.get(Vertx.currentContext(), CurrentContext::new);
        currentContext.put(String.class, "context");

        return next.startCall(call, headers);
    }

}
