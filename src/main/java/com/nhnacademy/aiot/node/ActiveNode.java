package com.nhnacademy.aiot.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ActiveNode extends Node implements Runnable {
    protected final Logger logger = LoggerFactory.getLogger(getName());

    private final Thread thread;

    protected ActiveNode(String id, String name) {
        super(id, name);
        thread = new Thread(this, getName());
    }

    private Thread getThread() {
        return thread;
    }

    public void start() {
        if (!isAlive()) {
            getThread().start();
        }
    }

    public void interrupt() {
        if (isAlive()) {
            getThread().interrupt();
        }
    }

    public boolean isAlive() {
        return getThread().isAlive();
    }

    @Override
    public void run() {
        preprocess();

        while (isAlive()) {
            process();
        }

        postprocess();
    }

    public abstract void preprocess();

    public abstract void process();

    public abstract void postprocess();
}
