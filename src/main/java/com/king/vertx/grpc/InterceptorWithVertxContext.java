package com.king.vertx.grpc;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.vertx.core.Vertx;

import static com.king.vertx.grpc.ContextLocalVertxServiceProvider.CUSTOM_CONTEXT_LOCAL;

public class InterceptorWithVertxContext implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        addToContext();

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(next.startCall(call, headers)) {
            @Override
            public void onMessage(ReqT message) {
//                addToContext();
                super.onMessage(message);
            }

            @Override
            public void onHalfClose() {
//                addToContext();
                super.onHalfClose();
            }

            @Override
            public void onReady() {
//                addToContext();
                super.onReady();
            }
        };
    }

    private static void addToContext() {
        var currentContext = Vertx.currentContext().getLocal(CUSTOM_CONTEXT_LOCAL, CurrentContext::new);
        currentContext.put(String.class, "vertx");
    }

}
