package model.components;

import model.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.SourceIcon;

public class Turbine extends Source {

    public Turbine(String name, Point position, boolean on) {
        super(name, position, on);
    }

    public void connectWire(Wire wire) {
        if (super.getOutputCount() < 2) {
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
        icon.setBoundingRect(getPosition(), 2, 4, .5, 0.5);
        return icon;
    }
}
