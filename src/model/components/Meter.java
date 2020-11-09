package model.components;

import model.geometry.Point;

public class Meter extends Device {
    public Meter(String name, Point position, Wire inWire, Wire outWire) {
        super(name, position);
    }
}
