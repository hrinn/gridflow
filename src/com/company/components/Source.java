package com.company.components;

import com.company.geometry.Point;

import java.util.List;

public class Source extends Component implements IToggleable {

    private Point position;
    private boolean on;

    public Source(String name, Point position) {
        super(name);
        this.position = position;
        this.on = false;
    }

    public void toggleState() {
        on = !on;
    }

    public boolean getState() {
        return on;
    }
}
