package com.company.components;

import com.company.geometry.Point;

public class Breaker extends Device implements IToggleable, ICloneable {

    private Voltage voltage;
    private boolean closed;

    public Breaker(String name, Point position, Component inComponent, Component outComponent, Voltage voltage, boolean closed) {
        super(name, position, inComponent, outComponent);
        this.voltage = voltage;
        this.closed = closed;

    }

    public void toggle() {
        this.closed = !this.closed;
    }

    public boolean getState() {
        return this.closed;
    }
}

enum Voltage
{
    KV12, KV70
}
