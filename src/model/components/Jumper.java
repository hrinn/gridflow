package model.components;

import model.geometry.Point;

public class Jumper extends Device {

    public Jumper(String name, Point position, Wire inWire, Wire outWire) {
        super(name, position);
    }
}
