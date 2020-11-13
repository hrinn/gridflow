package model.components;

import model.geometry.Point;

import java.util.List;

public class Device extends Component {

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
                outWire.energize();
                return List.of(outWire);
            }

            else{
                inWire.energize();
                return List.of(inWire);
            }
        }
        return List.of();
    }


}
