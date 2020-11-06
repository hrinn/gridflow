package com.company.components.device;

import com.company.components.IToggleable;
import com.company.components.Wire;
import com.company.components.device.Device;
import com.company.components.device.ILockable;
import com.company.geometry.Point;

public class Switch extends Device implements IToggleable, ILockable {

    boolean closed;
    boolean locked;

    public Switch(String name, Point position, Wire inWire, Wire outWire, boolean closed) {
        super(name, position, inWire, outWire);
        this.closed = closed;
        this.locked = false;
    }

    public void toggleState() {
        closed = !closed;
    }

    public boolean getState() {
        return closed;
    }

    public void toggleLocked() {
        locked = !locked;
    }
}
