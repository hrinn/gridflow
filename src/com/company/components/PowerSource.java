package com.company.components;

import com.company.geometry.Point;

import java.util.List;

public class PowerSource extends Component implements IToggleable{

    private boolean on;

    public PowerSource(String name, boolean energized, List<Component> connections, Point position, boolean on) {
        super(name, energized, connections, position);
        this.on = on;
    }

    public void toggle() {
        this.on = !on;
    }


    public boolean getState() {
        return on;
    }
}
