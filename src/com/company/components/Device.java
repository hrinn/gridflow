package com.company.components;

import com.company.geometry.Point;

public class Device extends Component {

    private Point position;
    private Component inComponent;
    private Component outComponent;

    public Device(String name, Point position, Component inComponent, Component outComponent) {
        super(name);
        this.position = position;
        this.inComponent = inComponent;
        this.outComponent = outComponent;
    }
}
