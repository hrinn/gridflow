package domain.components;

import domain.geometry.Point;

import java.util.List;
import java.util.UUID;

public class Device extends Component {

    //private Point position;
    private Wire inWire;
    private Wire outWire;

    public Device(String name, Point position) {
        super(name, position);
        this.inWire = null;
        this.outWire = null;
    }

    public Device(String name, Point position, UUID id, double angle, Wire inWire, Wire outWire) {
        super(name, position, id, angle);
        this.inWire = inWire;
        this.outWire = outWire;
    }

    public Wire getInWire() {
        return inWire;
    }

    public Wire getOutWire() {
        return outWire;
    }

    public void connectInWire(Wire inWire) {
        this.inWire = inWire;
    }

    public void connectOutWire(Wire outWire) {
        this.outWire = outWire;
    }

    public boolean isInWireEnergized() {
        return this.inWire.isEnergized();
    }

    public boolean isOutWireEnergized() {
        return this.outWire.isEnergized();
    }

    protected boolean checkClosed() {
        return true;
    }

    @Override
    public List<Component> getAccessibleConnections() {
        if(this.checkClosed()) {
            if(inWire.isEnergized()) {
                return List.of(outWire);
            }

            else{
                return List.of(inWire);
            }
        }
        return List.of();
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

    @Override
    public Component copy() {
        return new Device(getName(), getPosition(), getId(), getAngle(), (Wire)getInWire().copy(), (Wire)getOutWire().copy());
    }


}
