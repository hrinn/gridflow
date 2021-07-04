package domain.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import construction.ComponentType;
import construction.history.ComponentMemento;
import construction.properties.objectData.ComponentData;
import construction.properties.objectData.ObjectData;
import domain.Selectable;
import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;

import java.util.List;
import java.util.UUID;

public abstract class Component implements Selectable {

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

    public void setName(String name) { this.name = name; }

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

    public abstract ComponentType getComponentType();

    public ObjectData getComponentObjectData() {
        return new ComponentData(getComponentType(), name);
    }

    public abstract void updateComponentIcon();

    public abstract void updateComponentIconName();

    public ComponentIcon getUpdatedComponentIcon() {
        updateComponentIcon();
        return getComponentIcon();
    }

    public ComponentIcon getComponentIcon() {
        return icon;
    }

    public void setSelect(boolean select) {
        getComponentIcon().setSelect(select);
    }

    protected void setComponentIcon(ComponentIcon icon) {
        this.icon = icon;
    }

    public abstract List<Component> getAccessibleConnections();

    public abstract List<Component> getConnections();

    public abstract void setConnections(List<Component> connections);

    public ObjectNode getObjectNode(ObjectMapper mapper) {
        ObjectNode component = mapper.createObjectNode();
        component.put("id", getId().toString());
        component.put("type", this.getClass().getSimpleName());
        component.put("name", getName());
        component.put("pos", getPosition().toString());
        component.put("angle", getAngle());
        // TODO: Add component name pos to the json file
        //component.put("namepos", this.getComponentIcon().getCurrentNamePos().toString())
        return component;
    }

    public abstract ComponentMemento makeSnapshot();

    public abstract void delete();

}
