package model.components;

import model.geometry.Point;
import visualization.components.ComponentIcon;
import visualization.components.ComponentIconCreator;
import visualization.components.DeviceIcon;

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

    @Override
    protected boolean checkClosed() {
        return closed;
    }

    @Override
    public ComponentIcon getComponentIcon() {
        DeviceIcon icon;
        if (voltage == Voltage.KV12) {
            icon = ComponentIconCreator.get12KVBreakerIcon(getPosition());
        } else {
            icon = ComponentIconCreator.get70KVBreakerIcon(getPosition());
        }
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
        return icon;
    }
}
