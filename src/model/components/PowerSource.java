package model.components;

import model.geometry.Point;

import java.util.List;

public class PowerSource extends Source {

    public PowerSource(String name, Point position) {
        super(name, position);
    }

    public void connectWire(Wire output) {
        if (super.getOutputCount() < 1) {
            super.addOutput(output);
        }
    }
}
