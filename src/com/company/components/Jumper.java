package com.company.components;

import com.company.geometry.Point;

public class Jumper extends Device {
    public Jumper(String name, Point position, Component inComponent, Component outComponent) {
        super(name, position, inComponent, outComponent);
    }
}
