package com.company.components.device;

import com.company.components.Wire;
import com.company.components.device.Device;
import com.company.geometry.Point;

public class Meter extends Device {
    public Meter(String name, Point position, Wire inWire, Wire outWire) {
        super(name, position, inWire, outWire);
    }
}
