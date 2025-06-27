# Vertx repro

A sample repro that shows how multipart with webclient + http2 fails. It works when client is set to http 1.1.

## Server

 * Starts server with a simple handler.

## Testing

`ServerTest` showcases the behaviour using webclient. Change the `ThreadingModel.WORKER` of the `HttpProxyVerticle` in `Server` to make the test pass. 
