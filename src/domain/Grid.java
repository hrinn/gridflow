package domain;

import construction.builder.GridBuilder;
import construction.history.AssociationMemento;
import construction.history.ComponentMemento;
import construction.history.GridMemento;
import construction.properties.objectData.ObjectData;
import domain.components.*;
import domain.geometry.Point;
import javafx.scene.shape.Rectangle;
import visualization.componentIcons.ComponentIcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Grid {

    private List<Component> components;
    private List<Association> associations;

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

    // Functions like this show that I should have made a parent class for associations and components
    // This is bad object oriented programming
    public ObjectData getObjectData(String ObjectID) {
        Component comp = getComponent(ObjectID);
        if (comp == null) {
            Association association = getAssociation(ObjectID);
            if (association == null) return null; // ID is invalid
            return association.getAssociationObjectData();
        } else {
            return comp.getComponentObjectData();
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

    public int countAssociations() {
        return associations.size();
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

    public GridMemento makeSnapshot() {
        return new GridSnapshot(components, associations);
    }

    public void restore(GridMemento memento) {
        // restore components and associations
        this.components = memento.getComponents();
        this.associations = memento.getAssociations();

        // link components together
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            List<Component> connections = ((GridSnapshot)memento)
                    .getComponentConnectionsIDsByIndex(i).stream() // gets list of IDs from the memento
                    .map(connectionID -> getComponent(connectionID)) // maps this to a list of components
                    .collect(Collectors.toList()); // converts the stream back to a list
            component.setConnections(connections);
        }
    }
}

class GridSnapshot implements GridMemento {

    private final List<ComponentMemento> componentMementos = new ArrayList<>();
    private final List<AssociationMemento> associationMementos = new ArrayList<>();

    public GridSnapshot(List<Component> components, List<Association> associations) {
        components.forEach(component -> componentMementos.add(component.makeSnapshot()));
        associations.forEach(association -> associationMementos.add(association.makeSnapshot()));
    }

    public List<Component> getComponents() {
        // maps each component memento to a component and returns it as a list
        return componentMementos.stream().map(cm -> cm.getComponent()).collect(Collectors.toList());
    }

    // returns a list of IDs for the components that component i is connected to
    public List<String> getComponentConnectionsIDsByIndex(int i) {
        return componentMementos.get(i).getConnectionIDs();
    }

    public List<Association> getAssociations() {
        // maps each association memento to an association and returns it as a list
        return associationMementos.stream()
                .map(am -> ((AssociationSnapshot)am).getAssociation())
                .collect(Collectors.toList());
    }
}
