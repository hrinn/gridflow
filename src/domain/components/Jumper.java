package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

public class Jumper extends Device implements ICloseable {

    private boolean closed;
    private boolean closedByDefault;
    private DeviceIcon icon;

    public Jumper(String name, Point position, boolean closedByDefault) {
        super(name, position);
        this.closedByDefault = closedByDefault;
        this.closed = closedByDefault;
        createComponentIcon();
    }

    private void createComponentIcon() {
        icon = ComponentIconCreator.getJumperIcon(getPosition(), isClosed());
        icon.setDeviceEnergyStates(false, false);
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setAngle(getAngle(), getPosition());
    }

    @Override
    public ComponentIcon getComponentIcon() {
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
        return icon;
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean isClosedByDefault() {
        return closedByDefault;
    }

    public void toggle() {
        closed = !closed;
        createComponentIcon();
    }
}
