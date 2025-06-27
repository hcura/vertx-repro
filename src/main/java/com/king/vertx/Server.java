package com.king.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class Server {

    Vertx vertx;

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new HttpProxyVerticle(), new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER)).await();
    }

    public void stop() {
        vertx.close().await();
    }

    public static class HttpProxyVerticle extends AbstractVerticle {
        @Override
        public void start(Promise<Void> startPromise) {
            // server
            var httpServer = vertx.createHttpServer();

            // router
            Router router = Router.router(vertx);
            httpServer.requestHandler(router);

            router.route("/upload").handler(routingContext -> routingContext.end("the end"));

            // start
            httpServer.listen(8081).onComplete(event -> {
                if (event.succeeded()) {
                    System.out.println("SERVER: started on port 8081");
                    startPromise.complete();
                } else {
                    startPromise.fail(event.cause());
                }
            });
        }
    }

}