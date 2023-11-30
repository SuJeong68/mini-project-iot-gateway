package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.wire.Wire;
import java.util.ArrayList;
import java.util.List;

public abstract class OutNode extends ActiveNode implements Outable {

    private final List<Wire> inWire;

    protected OutNode(String id, String name) {
        super(id, name);
        inWire = new ArrayList<>();
    }

    public int getInWireCount() {
        return inWire.size();
    }

    public Wire getInWire(int index) {
        return inWire.get(index);
    }

    public void addInWire(Wire wire) {
        inWire.add(wire);
    }
}
