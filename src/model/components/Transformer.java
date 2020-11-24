package model.components;

import model.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

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
