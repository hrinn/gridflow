package domain.components;

import application.Globals;
import domain.geometry.Point;
import domain.geometry.Rectangle;
import javafx.scene.paint.Color;
import visualization.componentIcons.ComponentIcon;

import java.util.List;
import java.util.UUID;

public class Component {

    private UUID id;
    private String name;

    // in the future, this may move up since wire will not have a position
    // right now wire is just a node in a graph
    private Point position;
    private double unitWidth;
    private double unitHeight;
    private double unitWidthPadding;
    private double unitHeightPadding;

    public Component(String name, Point position) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.position = position;
        this.unitWidthPadding = -0.5;
        this.unitHeightPadding = -0.5;
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

    public double getUnitHeight() {
        return unitHeight;
    }

    public double getUnitWidth() {
        return unitWidth;
    }

    public void setUnitHeight(double unitHeight) {
        this.unitHeight = unitHeight;
    }

    public void setUnitWidth(double unitWidth) {
        this.unitWidth = unitWidth;
    }

    public double getUnitWidthPadding() {
        return unitWidthPadding;
    }

    public void setUnitWidthPadding(double unitWidthPadding) {
        this.unitWidthPadding = unitWidthPadding;
    }

    public double getUnitHeightPadding() {
        return unitHeightPadding;
    }

    public void setUnitHeightPadding(double unitHeightPadding) {
        this.unitHeightPadding = unitHeightPadding;
    }

    public Rectangle getBoundingRect() {
        double width = unitWidth * Globals.UNIT;
        double height = unitHeight * Globals.UNIT;

        // negative padding makes the clickable box a bit smaller than the actual unit size rectangle
        double widthPadding = unitWidthPadding * Globals.UNIT;
        double heightPadding = unitHeightPadding * Globals.UNIT;

        double adjustedWidth = width + widthPadding;
        double adjustedHeight = height + heightPadding;

        Point center = position.translate(0, height/2);

        Point topLeft = center.translate(-adjustedWidth/2, -adjustedHeight/2);
        Point bottomRight = center.translate(adjustedWidth/2, adjustedHeight/2);
        return new Rectangle(topLeft, bottomRight);
    }
}
