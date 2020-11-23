package model.components;

import model.geometry.Point;
import visualization.components.ComponentIcon;
import visualization.components.ComponentIconCreator;
import visualization.components.SourceIcon;

import java.util.List;

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
        icon.setSourceNodeEnergyState(getState());
        for (int i = 0; i < getOutputCount(); i++) {
            icon.setWireEnergyState(isOutputEnergized(i), i);
        }
        return icon;
    }
}
