package model.components;

import model.geometry.Point;
import visualization.components.ComponentIcon;
import visualization.components.ComponentIconCreator;
import visualization.components.SourceIcon;

public class PowerSource extends Source {

    public PowerSource(String name, Point position, boolean on) {
        super(name, position, on);
    }

    public void connectWire(Wire output) {
        if (super.getOutputCount() < 1) {
            super.addOutput(output);
        }
    }

    @Override
    public ComponentIcon getComponentIcon() {
        SourceIcon icon = ComponentIconCreator.getPowerSourceIcon(getPosition());
        icon.setSourceNodeEnergyState(getState());
        for (int i = 0; i < getOutputCount(); i++) {
            icon.setWireEnergyState(isOutputEnergized(i), i);
        }
        return icon;
    }
}
