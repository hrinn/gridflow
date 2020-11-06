package com.company.components;

import com.company.geometry.Point;

import java.util.List;

public class Turbine extends Source {

    private Component output1;
    private Component output2;

    public Turbine(String name, Point position, Component output1, Component output2) {
        super(name, position);
        output1 = output1;
        output2 = output2;
    }
}
