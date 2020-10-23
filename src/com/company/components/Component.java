package com.company.components;

import com.company.geometry.Point;

import java.util.List;
import java.util.UUID;

public class Component {

    private UUID id;
    private String name;
    private boolean energized;
    private List<Component> connections;
    private Point position;

    public Component(String name, boolean energized, List<Component> connections, Point position) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.energized = energized;
        this.connections = connections;
        this.position = position;
    }

}
