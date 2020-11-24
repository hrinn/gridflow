package model.components;

import model.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

public class Cutout extends Device implements ILockable, IToggleable {

    private boolean locked;
    private boolean closed;

    public Cutout(String name, Point position, Wire inWire, Wire outWire) {
        super(name, position);
    }

    public void toggleLocked() {
        locked = !locked;
    }

    @Override
    public ComponentIcon getComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getCutoutIcon(getPosition(), closed);
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
        icon.setComponentIconID(getId().toString());
        icon.setBoundingRect(getPosition(), 2, 3);
        return icon;
    }

    @Override
    public void toggleState() {
        closed = !closed;
    }

    @Override
    public boolean getState() {
        return closed;
    }
}
