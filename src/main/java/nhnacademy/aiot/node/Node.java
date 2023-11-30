package nhnacademy.aiot.node;

import java.util.UUID;

public abstract class Node {
    private final UUID uuid;
    private final String name;

    protected Node(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
    }

    protected String getUuid() {
        return uuid.toString();
    }

    protected String getName() {
        return name;
    }
}
