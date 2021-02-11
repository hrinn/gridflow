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
    private final List<Association> associations;

    public Grid() {
        components = new ArrayList<>();
        associations = new ArrayList<>();
    }

    public List<Component> getComponents() {
        return components;
    }

    public List<Association> getAssociations() {
        return associations;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void addComponents(Component... components) {
        this.components.addAll(Arrays.asList(components));
    }

    public void addAssociation(Association association) {
        associations.add(association);
    }

    public void deleteSelectedItem(String ID) {
        Component comp = getComponent(ID);
        if (comp == null) {
            Association association = getAssociation(ID);
            if (association != null) {
                deleteAssociation(association);
            }
        } else {
            deleteComponent(comp);
        }
    }

    private void deleteComponent(Component component) {
        try {
            component.delete();
            if(component instanceof Wire) {
                removeCausedBridgePoints((Wire) component);
            }
            components.remove(component);
        } catch (UnsupportedOperationException e) {
            System.err.println("Cannot delete Wire: " + component.getId());
        }
    }

    // gets a selectable by ID, returns null if not found
    public Selectable getSelectableByID(String ID) {
        Selectable selectable = getComponent(ID);
        if (selectable == null) {
            selectable = getAssociation(ID);
        }
        return selectable;
    }

    private void deleteAssociation(Association association) {
        associations.remove(association);
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

    public void clearGrid() {
        components.clear();
        associations.clear();
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

    public Association getAssociation(String id) {
        return associations.stream()
                .filter(association -> association.getID().toString().equals(id))
                .findFirst().orElse(null);
    }
}
