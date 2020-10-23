package com.company.components;

import com.company.geometry.Point;

import java.util.List;

public class Breaker extends Component implements IToggleable, ICloneable {

    private Voltage voltage;
    private boolean closed;

    public Breaker(String name, boolean energized, List<Component> connections, Point position, Voltage voltage, boolean closed) {
        super(name, energized, connections, position);
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
