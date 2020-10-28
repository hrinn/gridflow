package com.company.components;

import com.company.geometry.Line;

import java.util.List;

public class Wire extends Component {

    private List<Component> connections;
    private List<Line> segments;

    public Wire(String name, List<Component> connections) {
        super(name);
        this.connections = connections;
    }
}
