package com.king.vertx;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerTest {

    @Test
    void client() {
        var server = new Server();
        server.start();

        var client = WebClient.create(Vertx.vertx());

        client.getAbs("http://localhost:8081/proxy/test")
                .send()
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        System.out.println("Response: " + ar.result().bodyAsString());
                        assertEquals(200, ar.result().statusCode());
                    } else {
                        System.err.println("Failed to get response: " + ar.cause());
                    }
                }).await();

        server.stop();
    }

}