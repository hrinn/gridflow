package model.components;

import model.geometry.Point;

public class Cutout extends Device implements ILockable {

    private boolean locked;

    public Cutout(String name, Point position, Wire inWire, Wire outWire) {
        super(name, position);
    }

    public void toggleLocked() {
        locked = !locked;
    }
}
