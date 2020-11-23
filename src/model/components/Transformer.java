package model.components;

import model.geometry.Point;
import visualization.components.ComponentIcon;
import visualization.components.ComponentIconCreator;
import visualization.components.DeviceIcon;

public class Transformer extends Device {

    public Transformer(String name, Point position) {
        super(name, position);
    }

    @Override
    public ComponentIcon getComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getTransformerIcon(getPosition());
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
        return icon;
    }
}
