package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

public class Jumper extends Device implements ICloseable {

    private boolean closed;
    private boolean closedByDefault;

    public Jumper(String name, Point position, boolean closedByDefault) {
        super(name, position);
        this.closedByDefault = closedByDefault;
        this.closed = closedByDefault;
        this.getDimensions().setWidth(2);
        this.getDimensions().setHeight(3);
    }

    @Override
    public ComponentIcon getComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getJumperIcon(getPosition(), closed);
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setBoundingRect(getDimensions(), getPosition());
        icon.setAngle(getAngle(), getPosition());
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
    }
}
