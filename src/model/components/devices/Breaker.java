package model.components.devices;

import model.components.*;
import model.components.geometry.Point;

public class Breaker extends Device implements IToggleable, ICloneable, IPairable {

    private Voltage voltage;
    private boolean closed;

    public Breaker(String name, Point position, Wire inWire, Wire outWire, Voltage voltage, boolean closed) {
        super(name, position, inWire, outWire);
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

enum Voltage
{
    KV12, KV70
}
