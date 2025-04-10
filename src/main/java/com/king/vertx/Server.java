package com.king.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.httpproxy.HttpProxy;
import io.vertx.httpproxy.interceptors.HeadInterceptor;

public class Server {

    Vertx proxy;
    Vertx origin;

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        proxy = Vertx.vertx();
        proxy.deployVerticle(new HttpProxyVerticle(), new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER)).await();

        origin = Vertx.vertx();
        origin.deployVerticle(new HttpOriginVerticle()).await();
    }

    public void stop() {
        origin.close().await();
        proxy.close().await();
    }

    public static class HttpProxyVerticle extends AbstractVerticle {
        @Override
        public void start(Promise<Void> startPromise) {
            // server
            var httpServer = vertx.createHttpServer();

            // router
            Router router = Router.router(vertx);
            httpServer.requestHandler(router);

            // proxy
            var proxy = HttpProxy.reverseProxy(vertx.createHttpClient())
                    .origin(8082, "localhost")
                    .addInterceptor(HeadInterceptor.builder()
                            .removingPathPrefix("/proxy")
                            .addingPathPrefix("/origin")
                            .build());

            router.route("/proxy/*")
                    .handler(routingContext -> proxy.handle(routingContext.request()));

            // start
            httpServer.listen(8081).onComplete(event -> {
                if (event.succeeded()) {
                    System.out.println("PROXY: started on port 8081");
                    startPromise.complete();
                } else {
                    startPromise.fail(event.cause());
                }
            });
        }
    }

    public static class HttpOriginVerticle extends AbstractVerticle {
        @Override
        public void start(Promise<Void> startPromise) {
            // server
            var httpServer = vertx.createHttpServer();

            // router
            Router router = Router.router(vertx);
            httpServer.requestHandler(router);

            router.route("/origin/*")
                    .handler(routingContext -> routingContext.end("proxied!"));

            // start
            httpServer.listen(8082).onComplete(event -> {
                if (event.succeeded()) {
                    System.out.println("ORIGIN: started on port 8082");
                    startPromise.complete();
                } else {
                    startPromise.fail(event.cause());
                }
            });
        }
    }

}