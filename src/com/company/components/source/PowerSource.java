package com.company.components.source;

import com.company.components.Wire;
import com.company.components.source.Source;
import com.company.geometry.Point;

import java.util.List;

public class PowerSource extends Source {

    public PowerSource(String name, Point position, Wire output) {
        super(name, position, List.of(output));
    }
}
