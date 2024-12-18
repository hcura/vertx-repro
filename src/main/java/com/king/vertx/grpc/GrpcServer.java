package com.king.vertx.grpc;

import io.grpc.ServerInterceptors;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.grpcio.server.GrpcIoServer;
import io.vertx.grpcio.server.GrpcIoServiceBridge;

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

        var sampleService = new SampleService();
        var intercepted = ServerInterceptors.intercept(sampleService, new SampleInterceptor());

        GrpcIoServiceBridge.bridge(intercepted).bind(grpcServer);

        router.route().consumes("application/grpc")
                .handler(routingContext -> {
                    System.out.println("new request " + routingContext.request().path());
                    grpcServer.handle(routingContext.request());
                });

        // start
        httpServer.listen(8081).await();
        System.out.println("server started on port 8081");
    }

}