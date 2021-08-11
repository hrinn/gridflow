package domain.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import construction.ComponentType;
import construction.history.ComponentMemento;
import construction.properties.objectData.BreakerData;
import construction.properties.objectData.ObjectData;
import domain.geometry.Point;
import visualization.componentIcons.BreakerIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

import java.util.List;
import java.util.UUID;

public class Breaker extends Closeable {

    private Voltage voltage;

    private String tandemID = null;

    public Breaker(String name, Point position, Voltage voltage, boolean closedByDefault, String tandemID) {
        super(name, position, closedByDefault);
        this.voltage = voltage;
        this.tandemID = tandemID;
        createComponentIcon();
    }

    public Breaker(BreakerSnapshot snapshot) {
        super(UUID.fromString(snapshot.id), snapshot.name, snapshot.pos, snapshot.angle, snapshot.closedByDefault, snapshot.closed, snapshot.locked, snapshot.namePos);
        voltage = snapshot.voltage;
        tandemID = snapshot.tandemid;
        createComponentIcon();
    }

    public Breaker(JsonNode node) {
        super(UUID.fromString(node.get("id").asText()), node.get("name").asText(),
                Point.fromString(node.get("pos").asText()), node.get("angle").asDouble(),
                node.get("closedByDefault").asBoolean(), node.get("closed").asBoolean(),
                node.get("locked").asBoolean(), node.get("namepos").asBoolean());
        voltage = Voltage.valueOf(node.get("voltage").asText());
        if(node.get("tandemid") == null) {
            tandemID = null;
        }
        else {
            tandemID = node.get("tandemid").asText();
            if(tandemID.equals("")){
                tandemID = null;
            }
        }
        createComponentIcon();
    }

    protected void createComponentIcon() {
        BreakerIcon icon;
        if (voltage == Voltage.KV12) {
            icon = ComponentIconCreator.get12KVBreakerIcon(getPosition(), isClosed(), isClosedByDefault(), isLocked());
        } else {
            icon = ComponentIconCreator.get70KVBreakerIcon(getPosition(), isClosed(), isClosedByDefault(), isLocked());
        }
        icon.setBreakerEnergyStates(isInWireEnergized(), isOutWireEnergized(), isClosed());
        icon.setComponentIconID(getId().toString());
        icon.setAngle(getAngle(), getPosition());
        icon.setComponentName(getName(), isNameRight());
        setComponentIcon(icon);
    }

    @Override
    public ComponentType getComponentType() {
        if (voltage == Voltage.KV12) {
            return ComponentType.BREAKER_12KV;
        } else {
            return ComponentType.BREAKER_70KV;
        }
    }

    @Override
    public ObjectData getComponentObjectData() {
        return new BreakerData(getName(), isNameRight(), isClosedByDefault(), getTandemID(), getAngle());
    }

    @Override
    public void updateComponentIcon() {
        BreakerIcon icon = (BreakerIcon)getComponentIcon();
        icon.setBreakerEnergyStates(isInWireEnergized(), isOutWireEnergized(), isClosed());
    }

    @Override
    public void updateComponentIconName() {
        DeviceIcon icon = (DeviceIcon)getComponentIcon();
        icon.setComponentName(getName(), isNameRight());
    }

    public Voltage getVoltage() {
        return voltage;
    }

    @Override
    public ObjectNode getObjectNode(ObjectMapper mapper) {
        ObjectNode breaker = super.getObjectNode(mapper);
        breaker.put("voltage", voltage.toString());
        breaker.put("tandemid", tandemID);
        return breaker;
    }

    @Override
    public ComponentMemento makeSnapshot() {
        return new BreakerSnapshot(getId().toString(), getName(), getAngle(), getPosition(), voltage, isClosed(), isClosedByDefault(),
                isLocked(), getInWireID().toString(), getOutWireID().toString(), getTandemID(), isNameRight());
    }


    @Override
    public void toggleState() {
        toggleClosed();
        createComponentIcon();
    }

    @Override
    public void toggleLockedState() {
        toggleLocked(); // Changes the locked state in the parent class (closeable)
        createComponentIcon(); // Updates the component icon to show the new state
    }

    public String getTandemID() {
        return tandemID;
    }

    public void setTandemID(String tandemid) {
        if (tandemid.equals("")) {
            this.tandemID = null;
        } else {
            this.tandemID = tandemid;
        }
    }

    public boolean hasTandem() {
        if(tandemID == null || tandemID.equals("")){
            return false;
        }
        return true;
    }
}

class BreakerSnapshot implements ComponentMemento {
    String id;
    String name;
    double angle;
    Point pos;
    Voltage voltage;
    boolean closed;
    boolean closedByDefault;
    boolean locked;
    String inNodeID;
    String outNodeID;
    String tandemid;
    boolean namePos;

    public BreakerSnapshot(String id, String name, double angle, Point pos, Voltage voltage, boolean closed, boolean closedByDefault, boolean locked, String inNodeID, String outNodeID, String tandemid, boolean namePos) {
        this.id = id;
        this.name = name;
        this.angle = angle;
        this.pos = pos.copy();
        this.voltage = voltage;
        this.closed = closed;
        this.closedByDefault = closedByDefault;
        this.locked = locked;
        this.inNodeID = inNodeID;
        this.outNodeID = outNodeID;
        this.tandemid = tandemid;
        this.namePos = namePos;
    }

    public Breaker getComponent() {
        return new Breaker(this);
    }

    @Override
    public List<String> getConnectionIDs() {
        return List.of(inNodeID, outNodeID);
    }
}
