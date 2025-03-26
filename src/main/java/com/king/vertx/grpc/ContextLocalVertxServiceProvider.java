package com.king.vertx.grpc;

import io.vertx.core.internal.VertxBootstrap;
import io.vertx.core.spi.VertxServiceProvider;
import io.vertx.core.spi.context.storage.ContextLocal;

/**
 * Register the local storage.
 */
public class ContextLocalVertxServiceProvider implements VertxServiceProvider {

  public static final ContextLocal<CurrentContext> CUSTOM_CONTEXT_LOCAL = ContextLocal.registerLocal(CurrentContext.class);

  @Override
  public void init(VertxBootstrap builder) {
  }
}
