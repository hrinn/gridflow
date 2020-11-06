package com.company.components.device;

import com.company.components.Wire;
import com.company.geometry.Point;

public class Cutout extends Device implements ILockable {

    private boolean locked;

    public Cutout(String name, Point position, Wire inWire, Wire outWire) {
        super(name, position, inWire, outWire);
    }

    public void toggleLocked() {
        locked = !locked;
    }
}
