package nhnacademy.aiot.node;

import nhnacademy.aiot.message.Message;
import nhnacademy.aiot.wire.Wire;

public abstract class InOutNode extends ActiveNode {

    private Wire[] inWire;
    private Wire[] outWire;

    protected InOutNode(String name, int inCount, int outCount) {
        super(name);
        inWire = new Wire[inCount];
        outWire = new Wire[outCount];
    }

    protected int getInWireCount() {
        return inWire.length;
    }

    private int getOutWireCount() {
        return outWire.length;
    }

    protected Wire getInWire(int index) {
        return inWire[index];
    }

    protected Wire getOutWire(int index) {
        return outWire[index];
    }

    public void setInWire(int index, Wire wire) {
        inWire[index] = wire;
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
