package com.company.components;

import com.company.geometry.Point;

import java.util.List;

public class Turbine extends Source {

    public Turbine(String name, Point position, List<Component> outputs) {
        super(name, position, outputs);
    }
}
