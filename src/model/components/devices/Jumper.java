package model.components.devices;

import model.components.Wire;
import model.components.geometry.Point;

public class Jumper extends Device {

    public Jumper(String name, Point position, Wire inWire, Wire outWire) {
        super(name, position, inWire, outWire);
    }
}
