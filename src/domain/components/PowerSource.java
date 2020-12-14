package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.SourceIcon;

public class PowerSource extends Source {

    public PowerSource(String name, Point position, boolean on) {
        super(name, position, on);
        this.setUnitHeightPadding(0);
        this.setUnitWidthPadding(0);
        this.setUnitWidth(2);
        this.setUnitHeight(2.75);
        this.setMaxOutputs(1);
    }

    public void connectWire(Wire output) {
        if (super.getOutputCount() < getMaxOutputs()) {
            super.addOutput(output);
        }
    }

    @Override
    public ComponentIcon getComponentIcon() {
        SourceIcon icon = ComponentIconCreator.getPowerSourceIcon(getPosition(), getName(), isOn());
        icon.setSourceNodeEnergyState(isOn());
        for (int i = 0; i < getOutputCount(); i++) {
            icon.setWireEnergyState(isOutputEnergized(i), i);
        }
        icon.setComponentIconID(getId().toString());
        icon.setBoundingRect(getBoundingRect());
        return icon;
    }
}
