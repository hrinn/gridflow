package model.components;

import model.geometry.Point;

public class Switch extends Device implements IToggleable, ILockable {

    boolean closed;
    boolean locked;

    public Switch(String name, Point position, Wire inWire, Wire outWire, boolean closed) {
        super(name, position);
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

    @Override
    protected boolean checkClosed() {
        return closed;
    }
}
