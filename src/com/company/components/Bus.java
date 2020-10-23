package com.company.components;

import com.company.geometry.Line;
import com.company.geometry.Point;

import java.util.List;

public class Bus extends Component{

    private List<Line> segments;

    public Bus(String name, boolean energized, List<Component> connections, Point position, List<Line> segments) {
        super(name, energized, connections, position);
        this.segments = segments;
    }
}
