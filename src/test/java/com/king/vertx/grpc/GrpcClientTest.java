package com.king.vertx.grpc;

import org.junit.jupiter.api.Test;

class GrpcClientTest {

    // This fails when running with ThreadingModel.WORKER or VIRTUAL_THREAD. Works with ThreadingModel.EVENT_LOOP.
    @Test
    void test() {
        new GrpcServer().start();

        new GrpcClient().start();
    }

}