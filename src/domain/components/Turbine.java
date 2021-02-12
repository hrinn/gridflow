package domain.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import construction.ComponentType;
import domain.geometry.Point;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.SourceIcon;

import java.util.List;
import java.util.UUID;

public class Turbine extends Source {

    private Wire outWire1; // this is on the top of the source when oriented up
    private Wire outWire2; // this is on the bottom of the source when oriented up

    public Turbine(String name, Point position, boolean on) {
        super(name, position, on);
        createComponentIcon();
    }

    public Turbine(JsonNode node) {
        super(UUID.fromString(node.get("id").asText()), node.get("name").asText(),
                Point.fromString(node.get("pos").asText()), node.get("angle").asDouble(),
                node.get("on").asBoolean());
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
    public void setConnections(List<Component> connections) {
        outWire1 = (Wire)connections.get(0);
        outWire2 = (Wire)connections.get(1);
    }

    @Override
    public ObjectNode getObjectNode(ObjectMapper mapper) {
        ObjectNode turbine = super.getObjectNode(mapper);
        turbine.put("outWire1", outWire1.getId().toString());
        turbine.put("outWire2", outWire2.getId().toString());
        return turbine;
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
        SourceIcon icon = ComponentIconCreator.getTurbineIcon(getPosition(), isOn());
        icon.setSourceNodeEnergyState(isOn());
        icon.setWireEnergyState(false, 0);
        icon.setWireEnergyState(false, 1);
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setAngle(getAngle(), getPosition());
        setComponentIcon(icon);
    }

    @Override
    public void updateComponentIcon() {
        SourceIcon icon = (SourceIcon)getComponentIcon();
        icon.setWireEnergyState(isOutWire1Energized(), 0);
        icon.setWireEnergyState(isOutWire2Energized(), 1);
    }

    public void updateComponentIconName() {
        SourceIcon icon = (SourceIcon)getComponentIcon();
        icon.setComponentName(getName());
    }

    @Override
    public ComponentType getComponentType() { return ComponentType.TURBINE; }

}
