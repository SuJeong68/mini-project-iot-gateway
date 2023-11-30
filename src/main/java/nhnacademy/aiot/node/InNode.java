package nhnacademy.aiot.node;

import nhnacademy.aiot.message.Message;
import nhnacademy.aiot.wire.Wire;

public abstract class InNode extends ActiveNode {

    private final Wire[] outWire;

    protected InNode(String name, int count) {
        super(name);
        outWire = new Wire[count];
    }

    private int getOutWireCount() {
        return outWire.length;
    }

    protected Wire getOutWire(int index) {
        return outWire[index];
    }

    public void setOutWire(int index, Wire wire) {
        outWire[index] = wire;
    }

    protected void output(Message message) {
        for (int i = 0; i < getOutWireCount(); i++) {
            getOutWire(i).add(message);
        }
    }
}
