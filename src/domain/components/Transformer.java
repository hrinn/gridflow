package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

import java.util.UUID;

public class Transformer extends Device {

    public Transformer(String name, Point position) {
        super(name, position);
        setDimensions();
    }

    public Transformer(String name, Point position, UUID id, double angle, Wire inWire, Wire outWire) {
        super(name, position, id, angle, inWire, outWire);
        setDimensions();
    }

    public void setDimensions() {
        this.getDimensions().setWidth(3);
        this.getDimensions().setHeight(3);
    }

    @Override
    public ComponentIcon getComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getTransformerIcon(getPosition());
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setBoundingRect(getDimensions(), getPosition());
        icon.setAngle(getAngle(), getPosition());
        return icon;
    }

    @Override
    public Component copy() {
        return new Transformer(getName(), getPosition(), getId(), getAngle(), getInWire(), getOutWire());
    }
}
