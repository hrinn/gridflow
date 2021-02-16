package domain.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import construction.history.Memento;
import construction.history.MementoType;
import domain.geometry.Point;
import visualization.componentIcons.BreakerIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

import java.util.UUID;

public class Breaker extends Closeable {

    private Voltage voltage;

    public Breaker(String name, Point position, Voltage voltage, boolean closedByDefault) {
        super(name, position, closedByDefault);
        this.voltage = voltage;
        createComponentIcon();
    }

    public Breaker(JsonNode node) {
        super(UUID.fromString(node.get("id").asText()), node.get("name").asText(),
                Point.fromString(node.get("pos").asText()), node.get("angle").asDouble(),
                node.get("closedByDefault").asBoolean(), node.get("closed").asBoolean());
        voltage = Voltage.valueOf(node.get("voltage").asText());
        createComponentIcon();
    }

    private void createComponentIcon() {
        BreakerIcon icon;
        if (voltage == Voltage.KV12) {
            icon = ComponentIconCreator.get12KVBreakerIcon(getPosition(), isClosed(), isClosedByDefault());
        } else {
            icon = ComponentIconCreator.get70KVBreakerIcon(getPosition(), isClosed(), isClosedByDefault());
        }
        icon.setComponentName(getName());
        icon.setBreakerEnergyStates(isInWireEnergized(), isOutWireEnergized(), isClosed());
        icon.setComponentIconID(getId().toString());
        icon.setAngle(getAngle(), getPosition());
        setComponentIcon(icon);
    }

    @Override
    public void updateComponentIcon() {
        BreakerIcon icon = (BreakerIcon)getComponentIcon();
        icon.setBreakerEnergyStates(isInWireEnergized(), isOutWireEnergized(), isClosed());
    }

    public Voltage getVoltage() {
        return voltage;
    }

    @Override
    public ObjectNode getObjectNode(ObjectMapper mapper) {
        ObjectNode breaker = super.getObjectNode(mapper);
        breaker.put("voltage", voltage.toString());
        return breaker;
    }

    @Override
    public Memento makeSnapshot() {
        return new BreakerSnapshot(getId().toString(), getName(), getAngle(), getPosition(), voltage, isClosed(), isClosedByDefault());
    }


    @Override
    public void toggle() {
        toggleClosed();
        createComponentIcon();
    }
}

class BreakerSnapshot implements Memento {
    private String id;
    private String name;
    private double angle;
    private Point pos;
    private Voltage voltage;
    private boolean closed;
    private boolean closedByDefault;

    public BreakerSnapshot(String id, String name, double angle, Point pos, Voltage voltage, boolean closed, boolean closedByDefault) {
        this.id = id;
        this.name = name;
        this.angle = angle;
        this.pos = pos.copy();
        this.voltage = voltage;
        this.closed = closed;
        this.closedByDefault = closedByDefault;
    }

    @Override
    public MementoType getType() {
        return MementoType.BREAKER;
    }
}
