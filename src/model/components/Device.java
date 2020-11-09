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


}
