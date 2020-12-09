package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

public class Switch extends Device implements ICloseable, ILockable {

    private boolean closed;
    private boolean closedByDefault;
    private boolean locked;

    public final double unitWidth = 2;
    public final double unitHeight = 3;


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
        DeviceIcon icon = ComponentIconCreator.getSwitchIcon(getPosition(), isClosed(), isClosedByDefault());
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setBoundingRect(getPosition(), unitWidth, unitHeight, -0.5, -0.5);
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
