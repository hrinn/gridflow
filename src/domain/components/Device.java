package domain.components;

import domain.geometry.Point;

import java.util.List;
import java.util.UUID;

public abstract class Device extends Component {

    //private Point position;
    private Wire inWire;
    private Wire outWire;

    public Device(String name, Point position) {
        super(name, position);
        this.inWire = null;
        this.outWire = null;
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
        if (this instanceof ICloseable && !((ICloseable)this).isClosed()) {
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


}
