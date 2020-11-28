package com.eventbus.demo;

import com.eventbus.impl.Subscribe;

public class EventListener {
    public String lastMessage;

    @Subscribe
    public void listen(String event) {
        lastMessage = event;
        System.out.println("Message:"+lastMessage);
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
