package com.king.vertx.grpc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CurrentContext {

    private final Map<Class<?>, Object> context = new HashMap<>();

    public <T> Optional<T> get(Class<T> type) {
        return Optional.ofNullable(type.cast(context.get(type)));
    }

    public <T> void put(Class<T> type, T value) {
        context.put(type, value);
    }

    public <T> void remove(Class<T> type) {
        context.remove(type);
    }

}
