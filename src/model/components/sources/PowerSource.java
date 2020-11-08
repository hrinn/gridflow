package model.components.sources;

import model.components.Wire;
import model.components.geometry.Point;

import java.util.List;

public class PowerSource extends Source {

    public PowerSource(String name, Point position, Wire output) {
        super(name, position, List.of(output));
    }
}
