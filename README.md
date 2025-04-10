# Vertx repro

A sample repro that shows how `http-proxy` behaves with different threading models.

Everything works with `event-loop` threading model but fails with `worker` threading model.

## Server

 * Starts two servers:
   * proxy: 8081
   * origin: 8082

Any call to `8081/proxy/*` gets proxied to  `8082/origin/*`.  

## Testing

`ServerTest` showcases the behaviour. Change the `ThreadingModel.WORKER` of the `HttpProxyVerticle` in `Server` to make the test pass. 
