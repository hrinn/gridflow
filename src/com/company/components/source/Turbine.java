package com.company.components.source;

import com.company.components.Wire;
import com.company.components.source.Source;
import com.company.geometry.Point;

import java.util.List;

public class Turbine extends Source {

    public Turbine(String name, Point position, Wire output1, Wire output2) {
        super(name, position, List.of(output1, output2));
    }
}
