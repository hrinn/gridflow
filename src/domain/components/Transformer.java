package domain.components;

import com.fasterxml.jackson.databind.JsonNode;
import construction.history.Memento;
import construction.history.MementoType;
import domain.geometry.Point;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

import java.util.UUID;

public class Transformer extends Device {

    public Transformer(String name, Point position) {
        super(name, position);
        createComponentIcon();
    }

    public Transformer(JsonNode node) {
        super(UUID.fromString(node.get("id").asText()), node.get("name").asText(),
                Point.fromString(node.get("pos").asText()), node.get("angle").asDouble());
        createComponentIcon();
    }

    private void createComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getTransformerIcon(getPosition());
        icon.setDeviceEnergyStates(false, false);
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setAngle(getAngle(), getPosition());
        setComponentIcon(icon);
    }

    @Override
    public Memento makeSnapshot() {
        return new TransformerSnapshot(getId().toString(), getName(), getAngle(), getPosition());
    }

    @Override
    public void updateComponentIcon() {
        DeviceIcon icon = (DeviceIcon)getComponentIcon();
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
    }
}

class TransformerSnapshot implements Memento {
    private String id;
    private String name;
    private double angle;
    private Point pos;

    public TransformerSnapshot(String id, String name, double angle, Point pos) {
        this.id = id;
        this.name = name;
        this.angle = angle;
        this.pos = pos.copy();
    }

    @Override
    public MementoType getType() {
        return MementoType.TRANSFORMER;
    }
}
