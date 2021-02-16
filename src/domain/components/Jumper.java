package domain.components;

import com.fasterxml.jackson.databind.JsonNode;
import construction.history.Memento;
import construction.history.MementoType;
import domain.geometry.Point;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

import java.util.UUID;

public class Jumper extends Closeable {


    public Jumper(String name, Point position, boolean closedByDefault) {
        super(name, position, closedByDefault);
        createComponentIcon();
    }

    public Jumper(JsonNode node) {
        super(UUID.fromString(node.get("id").asText()), node.get("name").asText(),
                Point.fromString(node.get("pos").asText()), node.get("angle").asDouble(),
                node.get("closedByDefault").asBoolean(), node.get("closed").asBoolean());
        createComponentIcon();
    }

    private void createComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getJumperIcon(getPosition(), isClosed());
        icon.setDeviceEnergyStates(false, false);
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setAngle(getAngle(), getPosition());
        setComponentIcon(icon);
    }

    @Override
    public void updateComponentIcon() {
        DeviceIcon icon = (DeviceIcon) getComponentIcon();
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
    }

    public void toggle() {
        toggleClosed();
        createComponentIcon();
    }

    @Override
    public Memento makeSnapshot() {
        return new JumperSnapshot(getId().toString(), getName(), getAngle(), getPosition(), isClosed(), isClosedByDefault());
    }
}

class JumperSnapshot implements Memento {
    private String id;
    private String name;
    private double angle;
    private Point pos;
    private boolean closed;
    private boolean closedByDefault;

    public JumperSnapshot(String id, String name, double angle, Point pos, boolean closed, boolean closedByDefault) {
        this.id = id;
        this.name = name;
        this.angle = angle;
        this.pos = pos.copy();
        this.closed = closed;
        this.closedByDefault = closedByDefault;
    }

    @Override
    public MementoType getType() {
        return MementoType.JUMPER;
    }
}
