package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.wire.Wire;

public interface Inable {
    int getOutWireCount();

    Wire getOutWire(int index);

    void addOutWire(Wire wire);

    void output(Message message);
}
