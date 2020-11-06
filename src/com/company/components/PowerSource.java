package com.company.components;

import com.company.geometry.Point;

import java.util.List;

public class PowerSource extends Source {

    private Component output;

    public PowerSource(String name, Point position, Component output) {
        super(name, position);
        output = output;
    }
}
