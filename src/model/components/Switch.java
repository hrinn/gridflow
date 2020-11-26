package model.components;

import model.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

public class Switch extends Device implements ICloseable, ILockable {

    boolean closed;
    boolean closedByDefault;
    boolean locked;

    public Switch(String name, Point position, boolean closedByDefault) {
        super(name, position);
        this.closedByDefault = closedByDefault;
        this.closed = closedByDefault;
        this.locked = false;
    }

    public void toggleLocked() {
        locked = !locked;
    }

    @Override
    protected boolean checkClosed() {
        return closed;
    }

    @Override
    public ComponentIcon getComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getSwitchIcon(this);
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
        icon.setComponentIconID(getId().toString());
        icon.setBoundingRect(getPosition(), 2, 3);
        return icon;
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
