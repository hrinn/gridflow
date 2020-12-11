package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

public class Transformer extends Device {

    public Transformer(String name, Point position) {
        super(name, position);
        this.setUnitWidth(3);
        this.setUnitHeight(3);
    }

    @Override
    public ComponentIcon getComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getTransformerIcon(getPosition());
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setBoundingRect(getPosition(), this.getUnitWidth(), this.getUnitHeight(), -0.5, -0.5);
        return icon;
    }
}
