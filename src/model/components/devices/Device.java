package model.components.devices;

import model.components.Component;
import model.components.Wire;
import model.components.geometry.Point;

public class Device extends Component {

    private Point position;
    private Wire inWire;
    private Wire outWire;

    public Device(String name, Point position, Wire inWire, Wire outWire) {
        super(name);
        this.position = position;
        this.inWire = inWire;
        this.outWire = outWire;
    }


}
