package model.components.devices;

import model.components.Wire;
import model.components.geometry.Point;

public class Cutout extends Device implements ILockable {

    private boolean locked;

    public Cutout(String name, Point position, Wire inWire, Wire outWire) {
        super(name, position, inWire, outWire);
    }

    public void toggleLocked() {
        locked = !locked;
    }
}
