package model.components;

import model.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

public class Jumper extends Device implements IToggleable {

    private boolean closed;

    public Jumper(String name, Point position, Wire inWire, Wire outWire) {
        super(name, position);
    }

    @Override
    public void toggleState() {
        closed = !closed;
    }

    @Override
    public boolean getState() {
        return closed;
    }

    @Override
    public ComponentIcon getComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getJumperIcon(getPosition(), closed);
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
        icon.setComponentIconID(getId().toString());
        icon.setBoundingRect(getPosition(), 2, 3);
        return icon;
    }
}
