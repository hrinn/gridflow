package domain.components;

import application.Globals;
import domain.geometry.Point;
import domain.geometry.Rectangle;
import visualization.componentIcons.ComponentIcon;

import java.util.List;
import java.util.UUID;

public class Component {

    private UUID id;
    private String name;
    private Point position;
    private double angle;
    private Dimensions dimensions;

    public Component(String name, Point position) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.position = position;
        this.dimensions = new Dimensions();
        this.angle = 0;
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

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        if (angle % 90 != 0) return;
        this.angle = angle;
    }

    public ComponentIcon getComponentIcon() {
        throw new UnsupportedOperationException();
    }

    public List<Component> getAccessibleConnections() {
        return List.of();
    }

    public Dimensions getDimensions() {
        return dimensions;
    }
}
