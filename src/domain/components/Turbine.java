package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.SourceIcon;

public class Turbine extends Source {

    public Turbine(String name, Point position, boolean on) {
        super(name, position, on);
        this.setUnitWidth(2);
        this.setUnitHeight(4);
        this.setMaxOutputs(2);
    }

    public void connectWire(Wire wire) {
        if (super.getOutputCount() < getMaxOutputs()) {
            super.addOutput(wire);
        }
    }

    @Override
    public ComponentIcon getComponentIcon() {
        SourceIcon icon = ComponentIconCreator.getTurbineIcon(getPosition());
        icon.setSourceNodeEnergyState(isOn());
        for (int i = 0; i < getOutputCount(); i++) {
            icon.setWireEnergyState(isOutputEnergized(i), i);
        }
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setBoundingRect(getBoundingRect());
        return icon;
    }
}
