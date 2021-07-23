package domain.components;

import com.fasterxml.jackson.databind.JsonNode;
import construction.ComponentType;
import construction.history.ComponentMemento;
import domain.geometry.Point;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

import java.util.List;
import java.util.UUID;

public class Jumper extends Closeable {


    public Jumper(String name, Point position, boolean closedByDefault) {
        super(name, position, closedByDefault);
        createComponentIcon();
    }

    public Jumper(JumperSnapshot snapshot) {
        super(UUID.fromString(snapshot.id), snapshot.name, snapshot.pos, snapshot.angle, snapshot.closedByDefault, snapshot.closed, snapshot.locked);
        createComponentIcon();
    }

    public Jumper(JsonNode node) {
        super(UUID.fromString(node.get("id").asText()), node.get("name").asText(),
                Point.fromString(node.get("pos").asText()), node.get("angle").asDouble(),
                node.get("closedByDefault").asBoolean(), node.get("closed").asBoolean(), node.get("locked").asBoolean());
        createComponentIcon();
    }

    protected void createComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getJumperIcon(getPosition(), isClosed(), isLocked());
        icon.setDeviceEnergyStates(false, false);
        icon.setComponentIconID(getId().toString());
        icon.setAngle(getAngle(), getPosition());
        icon.setComponentName(getName(), isNameRight());
        setComponentIcon(icon);
    }

    @Override
    public ComponentType getComponentType() { return ComponentType.JUMPER; }

    @Override
    public void updateComponentIcon() {
        DeviceIcon icon = (DeviceIcon) getComponentIcon();
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
    }

    @Override
    public void updateComponentIconName() {
        DeviceIcon icon = (DeviceIcon)getComponentIcon();
        icon.setComponentName(getName(), isNameRight());
    }

    @Override
    public void toggleState() {
        toggleClosed();
        createComponentIcon();
    }

    @Override
    public void toggleLockedState() {
        toggleLocked();
        createComponentIcon();
    }

    @Override
    public ComponentMemento makeSnapshot() {
        return new JumperSnapshot(getId().toString(), getName(), getAngle(), getPosition(), isClosed(), isClosedByDefault(), isLocked(), getInWireID().toString(), getOutWireID().toString());
    }
}

class JumperSnapshot implements ComponentMemento {
    String id;
    String name;
    double angle;
    Point pos;
    boolean closed;
    boolean closedByDefault;
    boolean locked;
    String inNodeId;
    String outNodeId;

    public JumperSnapshot(String id, String name, double angle, Point pos, boolean closed, boolean closedByDefault, boolean locked, String inNodeId, String outNodeId) {
        this.id = id;
        this.name = name;
        this.angle = angle;
        this.pos = pos.copy();
        this.closed = closed;
        this.closedByDefault = closedByDefault;
        this.locked = locked;
        this.inNodeId = inNodeId;
        this.outNodeId = outNodeId;
    }

    @Override
    public Component getComponent() {
        return new Jumper(this);
    }

    @Override
    public List<String> getConnectionIDs() {
        return List.of(inNodeId, outNodeId);
    }
}
