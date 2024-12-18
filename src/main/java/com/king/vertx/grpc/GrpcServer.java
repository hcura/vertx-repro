package com.king.vertx.grpc;

import io.grpc.ServerInterceptors;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.grpcio.server.GrpcIoServer;
import io.vertx.grpcio.server.GrpcIoServiceBridge;

import static com.king.vertx.grpc.ContextLocalVertxServiceProvider.CONTEXT_LOCAL;

public class GrpcServer {
    public static void main(String[] args) {
        new GrpcServer().start();
    }

    private void start() {
        // server
        var vertx = Vertx.vertx();
        var httpServer = vertx.createHttpServer();

        // router
        Router router = Router.router(vertx);
        httpServer.requestHandler(router);

        // grpc
        var grpcServer = GrpcIoServer.server(vertx);

        var service = new SampleService().bindService();
//        service = ServerInterceptors.intercept(service, new SampleInterceptor());

        GrpcIoServiceBridge.bridge(service).bind(grpcServer);

        router.route().consumes("application/grpc")
                .handler(routingContext -> {
                    var currentContext = CONTEXT_LOCAL.get(Vertx.currentContext(), CurrentContext::new);
                    currentContext.put(String.class, "context");

                    routingContext.next();
                })
                .handler(routingContext -> {
                    System.out.println("new request " + routingContext.request().path());
                    grpcServer.handle(routingContext.request());
                });

        // start
        httpServer.listen(8081).await();
        System.out.println("server started on port 8081");
    }

}