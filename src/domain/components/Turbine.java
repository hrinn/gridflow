package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.SourceIcon;

import java.util.List;
import java.util.UUID;

public class Turbine extends Source {

    private Wire outWire1; // this is on the top of the source when oriented up
    private Wire outWire2; // this is on the bottom of the source when oriented up

    public Turbine(String name, Point position, boolean on) {
        super(name, position, on);
    }

    public void connectTopOutput(Wire output) {
        outWire1 = output;
    }

    public void connectBottomOutput(Wire output) {
        outWire2 = output;
    }

    @Override
    public List<Component> getConnections() {
        return List.of(outWire1, outWire2);
    }

    @Override
    public void delete() {
        outWire1.disconnect(getId());
        outWire2.disconnect(getId());
    }

    @Override
    public ComponentIcon getComponentIcon() {
        SourceIcon icon = ComponentIconCreator.getTurbineIcon(getPosition());
        icon.setSourceNodeEnergyState(isOn());
        icon.setWireEnergyState(outWire1.isEnergized(), 0);
        icon.setWireEnergyState(outWire2.isEnergized(), 1);
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setAngle(getAngle(), getPosition());
        return icon;
    }
}
