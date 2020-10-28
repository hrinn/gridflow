package com.company.components;

import com.company.geometry.Point;

public class Switch extends Device implements IToggleable, ILockable {

    boolean closed;
    boolean locked;

    public Switch(String name, Point position, Component inComponent, Component outComponent, boolean closed) {
        super(name, position, inComponent, outComponent);
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
