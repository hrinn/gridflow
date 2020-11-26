package model.components;

import model.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

public class Breaker extends Device implements ICloseable, ICloneable, IPairable {

    private Voltage voltage;
    private boolean closed;
    private boolean closedByDefault;

    public Breaker(String name, Point position, Voltage voltage, boolean closedByDefault) {
        super(name, position);
        this.voltage = voltage;
        this.closed = closedByDefault;
        this.closedByDefault = closedByDefault;

    } @Override
    protected boolean checkClosed() {
        return closed;
    }

    @Override
    public ComponentIcon getComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getBreakerIcon(this);
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
        icon.setComponentIconID(getId().toString());
        return icon;
    }

    public Voltage getVoltage() {
        return voltage;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public boolean isClosedByDefault() {
        return closedByDefault;
    }

    @Override
    public void toggle() {
        closed = !closed;
    }
}
