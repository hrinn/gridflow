package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;

import java.util.List;
import java.util.UUID;

public class Component {

    private UUID id;
    private String name;

    // in the future, this may move up since wire will not have a position
    // right now wire is just a node in a graph
    private Point position;

    public Component(String name, Point position) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.position = position;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Point getPosition() {
        return position;
    }

    public ComponentIcon getComponentIcon() {
        throw new UnsupportedOperationException();
    }

    public List<Component> getAccessibleConnections() {
        return List.of();
    }
}