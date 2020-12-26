package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

public class Breaker extends Device implements ICloseable, ICloneable, IPairable {

    private Voltage voltage;
    private boolean closed;
    private boolean closedByDefault;
    private DeviceIcon icon;

    public Breaker(String name, Point position, Voltage voltage, boolean closedByDefault) {
        super(name, position);
        this.voltage = voltage;
        this.closed = closedByDefault;
        this.closedByDefault = closedByDefault;
        createComponentIcon();
    }

    private void createComponentIcon() {
        if (voltage == Voltage.KV12) {
            icon = ComponentIconCreator.get12KVBreakerIcon(getPosition(), isClosed(), isClosedByDefault());
        } else {
            icon = ComponentIconCreator.get70KVBreakerIcon(getPosition(), isClosed(), isClosedByDefault());
        }
        icon.setComponentName(getName());
        icon.setDeviceEnergyStates(false, false);
        icon.setComponentIconID(getId().toString());
        icon.setAngle(getAngle(), getPosition());
    }

    @Override
    protected boolean checkClosed() {
        return closed;
    }

    @Override
    public ComponentIcon getComponentIcon() {
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
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
        createComponentIcon();
    }
}
