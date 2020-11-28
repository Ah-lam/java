package com.eventbus.impl;


import java.util.concurrent.Executor;

public class AsyncEventBus extends com.eventbus.impl.EventBus {
    public AsyncEventBus(Executor executor) {
        super(executor);
    }
}
