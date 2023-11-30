package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.wire.Wire;

public interface Outable {
    int getInWireCount();

    Wire getInWire(int index);

    void addInWire(Wire wire);
}
