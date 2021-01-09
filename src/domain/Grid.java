package domain;

import construction.builder.GridBuilder;
import domain.components.Component;
import domain.components.Source;
import domain.components.Wire;
import domain.geometry.Point;
import javafx.scene.shape.Rectangle;
import visualization.componentIcons.ComponentIcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Grid {

    private final List<Component> components;

    public Grid() {
        components = new ArrayList<>();
    }

    public List<Component> getComponents() {
        return components;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void addComponents(Component... components) {
        this.components.addAll(Arrays.asList(components));
    }

    public void deleteComponent(String ID) {
        Component component = getComponent(ID);
        if (component == null) return;
        if (component.getId().toString().equals(ID)) {
            try {
                component.delete();
                if(component instanceof Wire) {
                    removeCausedBridgePoints((Wire) component);
                }
                components.remove(component);
            } catch (UnsupportedOperationException e) {
                System.out.println("Cannot delete Wire: " + component.getId());
            }
        }
    }

    public void removeCausedBridgePoints(Wire wire) {
        // returns list of wires that conflict or null if a non-wire conflict occured.
        ArrayList<Wire> wireConflicts = new ArrayList<>();
        Rectangle currentComponentRect = wire.getComponentIcon().getFittingRect();

        List<ComponentIcon> existingComponents = this.getComponents().stream()
                .map(comp -> comp.getComponentIcon()).collect(Collectors.toList());

        for(ComponentIcon comp : existingComponents) {
            if (currentComponentRect.getBoundsInParent().intersects(comp.getFittingRect().getBoundsInParent())) {
                Component conflictingComponent = this.getComponent(comp.getID());
                if(conflictingComponent instanceof Wire) {
                    wireConflicts.add((Wire)conflictingComponent);
                }
            }
        }

        for(Wire conflictWire : wireConflicts) {
            Point conflictPoint = GridBuilder.getConflictPoint(wire, conflictWire);
            if(conflictPoint != null) {
                conflictWire.removeBridgePoint(conflictPoint);
            }
        }
    }

    public void loadComponents(List<Component> components) {
        for (Component component : components) addComponent(component);
    }

    public void clearComponents() {
        components.clear();
    }

    public List<Wire> getWires() {
        return components.stream()
                .filter(component -> component instanceof Wire)
                .map(component -> (Wire)component)
                .collect(Collectors.toList());
    }

    public List<Source> getSources() {
        return components.stream()
                .filter(component -> component instanceof Source)
                .map(component -> (Source)component)
                .collect(Collectors.toList());
    }

    public Component getComponent(String id) {
        return components.stream()
                .filter(comp -> comp.getId().toString().equals(id))
                .findFirst().orElse(null);
    }

}
