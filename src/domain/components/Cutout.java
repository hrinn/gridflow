package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

import java.util.UUID;

public class Cutout extends Device implements ILockable, ICloseable {

    private boolean locked;
    private boolean closed;
    private boolean closedByDefault;

    public Cutout(String name, Point position, boolean closedByDefault) {
        super(name, position);
        this.closedByDefault = closedByDefault;
        this.closed = closedByDefault;
        setDimensions();
    }

    public Cutout(String name, Point position, boolean closedByDefault, UUID id, double angle, Wire inWire, Wire outWire, boolean locked, boolean closed) {
        super(name, position, id, angle, inWire, outWire);
        this.closedByDefault = closedByDefault;
        this.locked = locked;
        this.closed = closed;
        setDimensions();
    }

    public void setDimensions() {
        this.getDimensions().setWidth(2);
        this.getDimensions().setHeight(3);
    }

    public void toggleLocked() {
        locked = !locked;
    }

    @Override
    public ComponentIcon getComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getCutoutIcon(getPosition(), closed);
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

    @Override
    public Component copy() {
        return new Cutout(getName(), getPosition(), closedByDefault, getId(), getAngle(), getInWire(), getOutWire(), locked, closed);
    }
}
