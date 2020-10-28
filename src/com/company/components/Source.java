package com.company.components;

import com.company.geometry.Point;

import java.util.List;

public class Source extends Component implements IToggleable {

    private Point position;
    private List<Component> outputs;
    private boolean on;

    public Source(String name, Point position, List<Component> outputs) {
        super(name);
        this.position = position;
        this.outputs = outputs;
        this.on = false;
    }

    public void toggleState() {
        on = !on;
    }

    public boolean getState() {
        return on;
    }
}
