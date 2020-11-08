package model.components.devices;

import model.components.IToggleable;
import model.components.Wire;
import model.components.geometry.Point;

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
