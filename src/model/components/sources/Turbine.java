package model.components.sources;

import model.components.Wire;
import model.components.geometry.Point;

import java.util.List;

public class Turbine extends Source {

    public Turbine(String name, Point position, Wire output1, Wire output2) {
        super(name, position, List.of(output1, output2));
    }
}
