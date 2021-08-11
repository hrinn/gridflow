package domain.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.geometry.Point;

import java.util.List;
import java.util.UUID;

public abstract class Device extends Component {

    //private Point position;
    private Wire inWire = null;
    private Wire outWire = null;

    public Device(String name, Point position) {
        super(name, position);
    }

    public Device(UUID id, String name, Point position, double angle, boolean nameRight) {
        super(id, name, position, angle, nameRight);
    }

    @Override
    public ObjectNode getObjectNode(ObjectMapper mapper) {
        ObjectNode device = super.getObjectNode(mapper);
        device.put("inWire", inWire.getId().toString());
        device.put("outWire", outWire.getId().toString());
        return device;
    }

    @Override
    public void setConnections(List<Component> connections) {
        inWire = (Wire)connections.get(0);
        outWire = (Wire)connections.get(1);
    }

    public void connectInWire(Wire inWire) {
        this.inWire = inWire;
    }

    public void connectOutWire(Wire outWire) {
        this.outWire = outWire;
    }

    public boolean isInWireEnergized() {
        if (inWire == null) return false;
        return inWire.isEnergized();
    }

    public boolean isOutWireEnergized() {
        if (outWire == null) return false;
        return outWire.isEnergized();
    }

    @Override
    public List<Component> getAccessibleConnections() {
        if (this instanceof Closeable && !((Closeable)this).isClosed()) {
            // If a closeable is open, there are no accessible connections
            return List.of();
        }

        if (isInWireEnergized()) {
            return List.of(outWire);
        } else {
            return List.of(inWire);
        }
    }

    @Override
    public List<Component> getConnections() {
        return List.of(inWire, outWire);
    }

    @Override
    public void delete() {
        inWire.disconnect(getId());
        outWire.disconnect(getId());
    }

    public UUID getInWireID() {
        return inWire.getId();
    }

    public UUID getOutWireID() {
        return outWire.getId();
    }


}
