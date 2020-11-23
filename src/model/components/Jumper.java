package model.components;

import model.geometry.Point;
import visualization.components.ComponentIcon;
import visualization.components.ComponentIconCreator;
import visualization.components.DeviceIcon;

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
        return icon;
    }
}
