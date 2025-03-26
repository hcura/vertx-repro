package com.king.vertx.grpc;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.vertx.core.Vertx;

import static com.king.vertx.grpc.ContextLocalVertxServiceProvider.CUSTOM_CONTEXT_LOCAL;

public class InterceptorWithGrpcContext implements ServerInterceptor {

    public static final Context.Key<String> GRPC_CONTEXT_KEY = Context.key("grpc");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        var context = Context.current().withValue(GRPC_CONTEXT_KEY, "grpc");

        return Contexts.interceptCall(context, call, headers, next);
    }


}
