package model.components.devices;

import model.components.Wire;
import model.components.geometry.Point;

public class Transformer extends Device {

    public Transformer(String name, Point position, Wire inWire, Wire outWire) {
        super(name, position, inWire, outWire);
    }
}
