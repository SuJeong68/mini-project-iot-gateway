package nhnacademy.aiot.wire;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import nhnacademy.aiot.message.Message;

public class Wire {
    private final BlockingQueue<Message> queue;

    public Wire() {
        this.queue = new LinkedBlockingQueue<>();
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
