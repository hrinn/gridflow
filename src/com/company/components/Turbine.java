package com.company.components;

import com.company.geometry.Point;

public class Turbine extends Device implements IToggleable {

    private boolean closed;

    public Turbine(String name, Point position, Component inComponent, Component outComponent, boolean closed) {
        super(name, position, inComponent, outComponent);
        this.closed = closed;
    }

    @Override
    public void toggle() {
        closed = !closed;
    }

    @Override
    public boolean getState() {
        return closed;
    }
}
