package com.company.components;

import com.company.geometry.Point;

public class Source extends Component implements IToggleable {

    private Point position;
    private Component outComponent;
    private boolean on;

    public Source(String name, Point position, Component outComponent) {
        super(name);
        this.position = position;
        this.outComponent = outComponent;
        this.on = false;
    }

    public void toggle() {
        on = !on;
    }

    public boolean getState() {
        return on;
    }
}
