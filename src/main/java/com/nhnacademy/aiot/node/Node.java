package com.nhnacademy.aiot.node;

public abstract class Node {
    private final String id;
    private final String name;

    protected Node(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    protected String getName() {
        return name;
    }
}
