package com.king.vertx.grpc;

import io.grpc.ServerInterceptors;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.grpcio.server.GrpcIoServer;
import io.vertx.grpcio.server.GrpcIoServiceBridge;

public class GrpcServer {
    public static void main(String[] args) {
        new GrpcServer().start();
    }

    public void start() {
        var vertx = Vertx.vertx();

        DeploymentOptions options = new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER);

        vertx.deployVerticle(new HttpVerticle(), options).await();
    }

    public static class HttpVerticle extends AbstractVerticle {
        @Override
        public void start(Promise<Void> startPromise) throws Exception {
            // server
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
                        System.out.println("Server: new request " + routingContext.request().path());
                        grpcServer.handle(routingContext.request());
                    });

            // start
            httpServer.listen(8081).onComplete(event -> {
                if (event.succeeded()) {
                    System.out.println("Server: started on port 8081");
                    startPromise.complete();
                } else {
                    startPromise.fail(event.cause());
                }
            });
        }
    }

}