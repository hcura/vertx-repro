package com.king.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpVersion;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.multipart.MultipartForm;
import org.junit.jupiter.api.Test;

import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerTest {

    @Test
    void client() {
        var server = new Server();
        server.start();

        var webClientOptions = new WebClientOptions()
//                .setProtocolVersion(HttpVersion.HTTP_1_1) // works
                .setProtocolVersion(HttpVersion.HTTP_2) // fails
                .setHttp2ClearTextUpgrade(false);

        var client = WebClient.create(Vertx.vertx(), webClientOptions);

        client.postAbs("http://localhost:8081/upload")
                .sendMultipartForm(MultipartForm.create().textFileUpload("file", "multipart.txt", Buffer.buffer("king\n"), TEXT_PLAIN.toString()))
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