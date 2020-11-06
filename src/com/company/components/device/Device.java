package com.company.components.device;

import com.company.components.Component;
import com.company.components.Wire;
import com.company.geometry.Point;

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
