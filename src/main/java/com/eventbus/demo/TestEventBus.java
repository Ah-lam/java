package com.eventbus.demo;

import com.eventbus.impl.EventBus;
import org.junit.Test;

public class TestEventBus {
    @Test
    public void testReceiveEvent() throws Exception {

        EventBus eventBus = new EventBus();
        EventListener listener = new EventListener();

        eventBus.register(listener);

        eventBus.post("first message");
        eventBus.post("second message");
        eventBus.post("third message");

        System.out.println("LastMessage:"+listener.getLastMessage());
    }
}
