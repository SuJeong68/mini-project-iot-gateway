package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.wire.Wire;
import java.util.ArrayList;
import java.util.List;

public abstract class InOutNode extends ActiveNode implements Inable, Outable {

    private List<Wire> inWire;
    private List<Wire> outWire;

    protected InOutNode(String id, String name) {
        super(id, name);
        inWire = new ArrayList<>();
        outWire = new ArrayList<>();
    }

    public int getInWireCount() {
        return inWire.size();
    }

    public int getOutWireCount() {
        return outWire.size();
    }

    public Wire getInWire(int index) {
        return inWire.get(index);
    }

    public Wire getOutWire(int index) {
        return outWire.get(index);
    }

    public void addInWire(Wire wire) {
        inWire.add(wire);
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
