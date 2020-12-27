package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

public class Cutout extends Device implements ILockable, ICloseable {

    private boolean locked;
    private boolean closed;
    private boolean closedByDefault;

    public Cutout(String name, Point position, boolean closedByDefault) {
        super(name, position);
        this.closedByDefault = closedByDefault;
        this.closed = closedByDefault;
        createComponentIcon();
    }

    public void toggleLocked() {
        locked = !locked;
    }

    private void createComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getCutoutIcon(getPosition(), closed);
        icon.setDeviceEnergyStates(false, false);
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setAngle(getAngle(), getPosition());
        setComponentIcon(icon);
    }

    @Override
    public void updateComponentIcon() {
        DeviceIcon icon = (DeviceIcon) getComponentIcon();
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
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
