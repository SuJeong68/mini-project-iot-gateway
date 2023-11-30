package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.wire.Wire;
import java.util.ArrayList;
import java.util.List;

public abstract class InNode extends ActiveNode implements Inable {

    private final List<Wire> outWire;

    protected InNode(String id, String name) {
        super(id, name);
        outWire = new ArrayList<>();
    }

    public int getOutWireCount() {
        return outWire.size();
    }

    public Wire getOutWire(int index) {
        return outWire.get(index);
    }

    public void addOutWire(Wire wire) {
        outWire.add(wire);
    }

    public void output(Message message) {
        for (int i = 0; i < getOutWireCount(); i++) {
            getOutWire(i).add(message);
        }
    }
}
