package domain.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;

import java.util.List;
import java.util.UUID;

public abstract class Component {

    private UUID id;
    private String name;
    private Point position;
    private double angle;
    private ComponentIcon icon;


    public Component(String name, Point position) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.position = position;
        this.angle = 0;
    }

    public Component (UUID id, String name, Point position, double angle) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.angle = angle;
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
        getUpdatedComponentIcon().setAngle(angle, getPosition());
    }

    public abstract void updateComponentIcon();

    public ComponentIcon getUpdatedComponentIcon() {
        updateComponentIcon();
        return getComponentIcon();
    }

    public ComponentIcon getComponentIcon() {
        return icon;
    }

    protected void setComponentIcon(ComponentIcon icon) {
        this.icon = icon;
    }

    public abstract List<Component> getAccessibleConnections();

    public abstract List<Component> getConnections();

    public ObjectNode getObjectNode(ObjectMapper mapper) {
        ObjectNode component = mapper.createObjectNode();
        component.put("id", getId().toString());
        component.put("type", this.getClass().getSimpleName());
        component.put("name", getName());
        component.put("x", getPosition().getX());
        component.put("y", getPosition().getY());
        component.put("angle", getAngle());
        return component;
    }

    public abstract void delete();

}
