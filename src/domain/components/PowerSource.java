package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.SourceIcon;

import java.util.List;

public class PowerSource extends Source {

    private Wire outWire; // this is on the bottom of the source when oriented up

    public PowerSource(String name, Point position, boolean on) {
        super(name, position, on);
        setDimensions();
    }

    private void setDimensions() {
        this.getDimensions().setPadding(0);
        this.getDimensions().setBottomPadding(-0.25);
        this.getDimensions().setWidth(2);
        this.getDimensions().setHeight(3);
    }

    public void connectWire(Wire outWire) {
        this.outWire = outWire;
    }

    @Override
    public List<Component> getOutputs() {
        return List.of(outWire);
    }

    @Override
    public ComponentIcon getComponentIcon() {
        SourceIcon icon = ComponentIconCreator.getPowerSourceIcon(getPosition(), getName(), isOn());
        icon.setSourceNodeEnergyState(isOn());
        icon.setWireEnergyState(outWire.isEnergized(), 0);
        icon.setComponentIconID(getId().toString());
        icon.setBoundingRect(getDimensions(), getPosition());
        icon.setAngle(getAngle(), getPosition());
        return icon;
    }
}
