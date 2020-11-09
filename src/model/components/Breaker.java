package model.components;

import model.geometry.Point;

public class Breaker extends Device implements IToggleable, ICloneable, IPairable {

    private Voltage voltage;
    private boolean closed;

    public Breaker(String name, Point position, Voltage voltage, boolean closed) {
        super(name, position);
        this.voltage = voltage;
        this.closed = closed;

    }

    public void toggleState() {
        this.closed = !this.closed;
    }

    public boolean getState() {
        return this.closed;
    }
}
