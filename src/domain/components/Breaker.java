package domain.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import construction.ComponentType;
import construction.history.ComponentMemento;
import domain.geometry.Point;
import visualization.componentIcons.BreakerIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

import java.util.List;
import java.util.UUID;

public class Breaker extends Closeable {

    private Voltage voltage;

    private String tandemid = null;

    public Breaker(String name, Point position, Voltage voltage, boolean closedByDefault, String tandemID) {
        super(name, position, closedByDefault);
        this.voltage = voltage;
        this.tandemid = tandemID;
        createComponentIcon();
    }

    public Breaker(BreakerSnapshot snapshot) {
        super(UUID.fromString(snapshot.id), snapshot.name, snapshot.pos, snapshot.angle, snapshot.closedByDefault, snapshot.closed);
        voltage = snapshot.voltage;
        tandemid = snapshot.tandemid;
        createComponentIcon();
    }

    public Breaker(JsonNode node) {
        super(UUID.fromString(node.get("id").asText()), node.get("name").asText(),
                Point.fromString(node.get("pos").asText()), node.get("angle").asDouble(),
                node.get("closedByDefault").asBoolean(), node.get("closed").asBoolean());
        voltage = Voltage.valueOf(node.get("voltage").asText());
        if(node.get("tandemid") == null) {
            tandemid = null;
        }
        else {
            tandemid = node.get("tandemid").asText();
            if(tandemid.equals("")){
                tandemid = null;
            }
        }
        createComponentIcon();
    }

    private void createComponentIcon() {
        BreakerIcon icon;
        if (voltage == Voltage.KV12) {
            icon = ComponentIconCreator.get12KVBreakerIcon(getPosition(), isClosed(), isClosedByDefault(), isLocked());
        } else {
            icon = ComponentIconCreator.get70KVBreakerIcon(getPosition(), isClosed(), isClosedByDefault(), isLocked());
        }
        icon.setComponentName(getName());
        icon.setBreakerEnergyStates(isInWireEnergized(), isOutWireEnergized(), isClosed());
        icon.setComponentIconID(getId().toString());
        icon.setAngle(getAngle(), getPosition());
        setComponentIcon(icon);
    }

    private void createIconToggle(Point namePos) {
        BreakerIcon icon;
        if (voltage == Voltage.KV12) {
            icon = ComponentIconCreator.get12KVBreakerIcon(getPosition(), isClosed(), isClosedByDefault(), isLocked());
        } else {
            icon = ComponentIconCreator.get70KVBreakerIcon(getPosition(), isClosed(), isClosedByDefault(), isLocked());
        }
        icon.setComponentNamePosition(namePos);
        icon.setComponentName(getName());
        icon.setBreakerEnergyStates(isInWireEnergized(), isOutWireEnergized(), isClosed());
        icon.setComponentIconID(getId().toString());
        icon.setAngle(getAngle(), getPosition());
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
    public void updateComponentIcon() {
        BreakerIcon icon = (BreakerIcon)getComponentIcon();
        icon.setBreakerEnergyStates(isInWireEnergized(), isOutWireEnergized(), isClosed());
    }

    @Override
    public void updateComponentIconName() {
        DeviceIcon icon = (DeviceIcon)getComponentIcon();
        icon.setComponentName(getName());
    }

    public Voltage getVoltage() {
        return voltage;
    }

    @Override
    public ObjectNode getObjectNode(ObjectMapper mapper) {
        ObjectNode breaker = super.getObjectNode(mapper);
        breaker.put("voltage", voltage.toString());
        breaker.put("tandemid", tandemid);
        return breaker;
    }

    @Override
    public ComponentMemento makeSnapshot() {
        return new BreakerSnapshot(getId().toString(), getName(), getAngle(), getPosition(), voltage, isClosed(), isClosedByDefault(),
                getInWireID().toString(), getOutWireID().toString(), getTandemID());
    }


    @Override
    public void toggleState() {
        Point oldNamePos = this.getComponentIcon().getCurrentNamePos();
        boolean oldActiveLeft = this.getComponentIcon().getActiveLeft();
        toggleClosed();
        createComponentIcon();
        this.getComponentIcon().setComponentNamePosition(oldNamePos);
        this.getComponentIcon().setCurrentNamePos(oldNamePos);
        this.getComponentIcon().setActiveLeft(oldActiveLeft);
    }

    @Override
    public void toggleLockedState() {
        toggleLocked(); // Changes the locked state in the parent class (closeable)
        createComponentIcon(); // Updates the component icon to show the new state
    }

    public String getTandemID() {
        return tandemid;
    }

    public void setTandemID(String tandemid) {
        if (tandemid.equals("")) {
            this.tandemid = null;
        } else {
            this.tandemid = tandemid;
        }
    }

    public boolean hasTandem() {
        if(tandemid == null || tandemid.equals("")){
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
    String inNodeID;
    String outNodeID;
    String tandemid;

    public BreakerSnapshot(String id, String name, double angle, Point pos, Voltage voltage, boolean closed, boolean closedByDefault, String inNodeID, String outNodeID, String tandemid) {
        this.id = id;
        this.name = name;
        this.angle = angle;
        this.pos = pos.copy();
        this.voltage = voltage;
        this.closed = closed;
        this.closedByDefault = closedByDefault;
        this.inNodeID = inNodeID;
        this.outNodeID = outNodeID;
        this.tandemid = tandemid;
    }

    public Breaker getComponent() {
        return new Breaker(this);
    }

    @Override
    public List<String> getConnectionIDs() {
        return List.of(inNodeID, outNodeID);
    }
}
