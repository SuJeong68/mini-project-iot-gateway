package nhnacademy.aiot.node;

import nhnacademy.aiot.wire.Wire;

public abstract class OutNode extends ActiveNode {

    private final Wire[] inWire;

    protected OutNode(String name, int count) {
        super(name);
        inWire = new Wire[count];
    }

    protected int getInWireCount() {
        return inWire.length;
    }

    protected Wire getInWire(int index) {
        return inWire[index];
    }

    public void setInWire(int index, Wire wire) {
        inWire[index] = wire;
    }
}
