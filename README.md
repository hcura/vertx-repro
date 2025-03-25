# Vertx repro

A sample repro that shows context behaviour when using GRPC interceptors. It's a much simpler version of what we have internally, but it includes the key components.

It's split into server + client.

## Server

 - Starts a server on port 8081 with a router (not needed, but it replicates our internal use case) + grpc handler.
 - Bridges a grpc service with an interceptor
 - Interceptor sets some context which is used by the service impl

## Client

Connects to the server and makes a simple call.

## Testing

### Storage/Context
By toggling the dependency `io.vertx:vertx-grpcio-context-storage`.

- The client returns `"context"` when the dependency is missing - which means the service is able to get the value from the context and return it
- The client returns `UNKNOWN` when the dependency is present - due to failing to find the context on the server side.

### CR5 issues with ThreadingModel.WORKER and EVENT_LOOP

- Run the `GrpClientTest` and change threading model accordingly in `GrpcServer`.
