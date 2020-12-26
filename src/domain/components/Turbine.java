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
    private SourceIcon icon;

    public Turbine(String name, Point position, boolean on) {
        super(name, position, on);
        createComponentIcon();
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
    public void toggle() {
        setOn(!isOn());
        createComponentIcon();
    }

    private boolean isOutWire1Energized() {
        if (outWire1 == null) return false;
        return outWire1.isEnergized();
    }

    private boolean isOutWire2Energized() {
        if (outWire2 == null) return false;
        return outWire2.isEnergized();
    }

    private void createComponentIcon() {
        icon = ComponentIconCreator.getTurbineIcon(getPosition());
        icon.setSourceNodeEnergyState(isOn());
        icon.setWireEnergyState(false, 0);
        icon.setWireEnergyState(false, 1);
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setAngle(getAngle(), getPosition());
    }

    @Override
    public ComponentIcon getComponentIcon() {
        icon.setWireEnergyState(isOutWire1Energized(), 0);
        icon.setWireEnergyState(isOutWire2Energized(), 1);
        return icon;
    }
}
