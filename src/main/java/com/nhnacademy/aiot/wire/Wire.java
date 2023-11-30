package com.nhnacademy.aiot.wire;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import com.nhnacademy.aiot.message.Message;

public class Wire {
    private final String id;
    private final BlockingQueue<Message> queue;

    public Wire(String id) {
        this.id = id;
        this.queue = new LinkedBlockingQueue<>();
    }

    public String getId() {
        return id;
    }

    private BlockingQueue<Message> getQueue() {
        return queue;
    }

    public void add(Message message) {
        getQueue().add(message);
    }

    public Message get() {
        return getQueue().poll();
    }

    public boolean hasMessage() {
        return !getQueue().isEmpty();
    }
}
