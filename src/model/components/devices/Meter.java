package model.components.devices;

import model.components.Wire;
import model.components.geometry.Point;

public class Meter extends Device {
    public Meter(String name, Point position, Wire inWire, Wire outWire) {
        super(name, position, inWire, outWire);
    }
}
