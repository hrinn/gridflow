package construction.builder;

import construction.PropertiesData;
import construction.ComponentType;
import domain.Grid;
import domain.components.*;
import domain.geometry.Point;
import javafx.scene.shape.Rectangle;
import visualization.componentIcons.ComponentIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GridBuilder {

    private Grid grid;
    private PropertiesData properties;

    public GridBuilder(Grid grid, PropertiesData properties) {
        this.grid = grid;
        this.properties = properties;
    }

    // This is what runs when a component is placed on the canvas standalone
    public boolean placeComponent(Point position, ComponentType componentType) {
        if (isDevice(componentType)) {
            return placeDevice(position, componentType);
        }
        else if (isSource(componentType)) {
            return placeSource(position, componentType);
        }
        return false;
    }

    // TODO: abstract conflictcomponent logic to it's own method to avoid duplicate code
    //  this is done in multiple places in this file.

    public boolean placeDevice(Point position, ComponentType componentType) {

        Device device = createDevice(position, componentType);
        if (device == null) return false;
        device.setAngle(properties.getRotation());

        if(!verifyPlacement(device)) return false;

        Wire inWire = new Wire(position);
        Component conflictComponent = verifySingleWirePosition(inWire);
        if(conflictComponent == null) { // use new wire
            device.connectInWire(inWire);
            inWire.connect(device);
            grid.addComponent(inWire);
        }
        else if (conflictComponent instanceof Wire){
            inWire = (Wire) conflictComponent;
            device.connectInWire(inWire);
            inWire.connect(device);
        }
        else{
            conflictComponent.getComponentIcon().showError();
            return false;
        }


        Point outPoint = position.translate(0, device.getComponentIcon().getHeight());
        Wire outWire = new Wire(outPoint.rotate(properties.getRotation(), position));
        conflictComponent = verifySingleWirePosition(outWire);
        if(conflictComponent == null) { // use new wire
            device.connectOutWire(outWire);
            outWire.connect(device);
            grid.addComponent(outWire);
        }
        else if (conflictComponent instanceof Wire){ // there is a wire conflict, connect this wire
            outWire = (Wire) conflictComponent;
            device.connectOutWire(outWire);
            outWire.connect(device);
        }
        else{
            conflictComponent.getComponentIcon().showError();
            return false;
        }

        grid.addComponent(device);
        return true;
    }

    public Device createDevice(Point point, ComponentType componentType) {
        return switch (componentType) {
            case TRANSFORMER -> new Transformer(properties.getName(), point);
            case BREAKER_12KV -> new Breaker(properties.getName(), point, Voltage.KV12, properties.getDefaultState());
            case BREAKER_70KV -> new Breaker(properties.getName(), point, Voltage.KV70, properties.getDefaultState());
            case JUMPER -> new Jumper(properties.getName(), point, properties.getDefaultState());
            case CUTOUT -> new Cutout(properties.getName(), point, properties.getDefaultState());
            case SWITCH -> new Switch(properties.getName(), point, properties.getDefaultState());
            default -> null;
        };
    }

    public boolean placeSource(Point position, ComponentType componentType) {

        switch (componentType) {
            case POWER_SOURCE -> {
                PowerSource powerSource = new PowerSource(properties.getName(), position, false);
                powerSource.setAngle(properties.getRotation());
                if(!verifyPlacement(powerSource)) return false;

                Wire outWire = new Wire(position);
                Component conflictComponent = verifySingleWirePosition(outWire);
                if(conflictComponent == null) { // use new wire
                    powerSource.connectWire(outWire);
                    outWire.connect(powerSource);
                    grid.addComponent(outWire);
                }
                else if (conflictComponent instanceof Wire){ // there is a wire conflict, connect this wire
                    outWire = (Wire) conflictComponent;
                    powerSource.connectWire(outWire);
                    outWire.connect(powerSource);
                }
                else{
                    conflictComponent.getComponentIcon().showError();
                    return false;
                }


                grid.addComponents(powerSource);
            }
            case TURBINE -> {
                Turbine turbine = new Turbine(properties.getName(), position, false);
                turbine.setAngle(properties.getRotation());
                if(!verifyPlacement(turbine)) return false;

                Wire topWire = new Wire(position);
                Component conflictComponent = verifySingleWirePosition(topWire);
                if(conflictComponent == null) { // use new wire
                    turbine.connectTopOutput(topWire);
                    topWire.connect(turbine);
                    grid.addComponent(topWire);
                }
                else if (conflictComponent instanceof Wire){
                    topWire = (Wire) conflictComponent;
                    turbine.connectTopOutput(topWire);
                    topWire.connect(turbine);
                }
                else{
                    conflictComponent.getComponentIcon().showError();
                    return false;
                }

                Point bottomPoint = position.translate(0, turbine.getComponentIcon().getHeight())
                        .rotate(turbine.getAngle(), position);
                Wire bottomWire = new Wire(bottomPoint);
                conflictComponent = verifySingleWirePosition(bottomWire);
                if(conflictComponent == null) { // use new wire
                    turbine.connectBottomOutput(bottomWire);
                    bottomWire.connect(turbine);
                    grid.addComponent(bottomWire);
                }
                else if (conflictComponent instanceof Wire){ // there is a wire conflict, connect this wire
                    bottomWire = (Wire) conflictComponent;
                    turbine.connectBottomOutput(bottomWire);
                    bottomWire.connect(turbine);
                }
                else{
                    conflictComponent.getComponentIcon().showError();
                    return false;
                }


                grid.addComponents(turbine);
            }
        }
        return true;
    }

    public boolean placeWire(Point startPosition, Point endPosition) {
        Wire wire = new Wire(startPosition, endPosition);
        List<Component> wireConflicts = verifyWirePlacement(wire);

        if (wireConflicts == null) {
            return false; // there was a non wire conflict
        } else if (wireConflicts.size() == 0) {
            grid.addComponent(wire); // nothing conflicted
        } else {
            // TODO: connect if ctrl is pressed
            // wires overlapped, connect to them
            /*
            for(Component conflictingComponent : wireConflicts) {
                if(conflictingComponent instanceof Wire)
                    ((Wire) conflictingComponent).connect(wire);
                wire.connect(conflictingComponent);
            }
            grid.addComponent(wire);
             */

            // Add wire with bridges
            for(Component conflictingComponent : wireConflicts) {
                if(conflictingComponent instanceof Wire) {
                    Point conflictPoint = getConflictPoint(wire, (Wire) conflictingComponent);
                    if(conflictPoint == null) {
                        return false;
                    }
                    wire.addBridgePoint(conflictPoint);
                }
            }
            grid.addComponent(wire);

        }
        return true;
    }

    public Point getConflictPoint(Wire wire1, Wire wire2) {
        //TODO: evaluate point wire placement/overlap

        System.out.println("WIRE1 START = (" + wire1.getStart().getX() + ", " + wire1.getStart().getY() + ")");
        System.out.println("WIRE1 END = (" + wire1.getEnd().getX() + ", " + wire1.getEnd().getY() + ")");

        System.out.println("WIRE2 START = (" + wire2.getStart().getX() + ", " + wire2.getStart().getY() + ")");
        System.out.println("WIRE2 END = (" + wire2.getEnd().getX() + ", " + wire2.getEnd().getY() + ")");

        if (wire1.getStart().getX() == wire1.getEnd().getX() && wire1.getStart().getY() == wire1.getEnd().getY()) {
            System.out.println("PLACING POINT WIRE;");
            return null;
        }
        else if (wire2.getStart().getX() == wire2.getEnd().getX() && wire2.getStart().getY() == wire2.getEnd().getY()) {
            System.out.println("CONFLICTING POINT WIRE;");
            return null;
        }
        else if (wire1.getStart().getX() == wire1.getEnd().getX() && wire2.getStart().getY() == wire2.getEnd().getY()) {
            System.out.println("PLACING HORIZONTAL WIRE;");
            System.out.println("BRIDGE AT (" + wire1.getStart().getX() + ", " + wire2.getStart().getY() + ")");
            return new Point(wire1.getStart().getX(), wire2.getStart().getY());
        }
        else if (wire1.getStart().getY() == wire1.getEnd().getY() && wire2.getStart().getX() == wire2.getEnd().getX()){
            System.out.println("PLACING VERTICAL WIRE;");
            System.out.println("BRIDGE AT (" + wire2.getStart().getX() + ", " + wire1.getStart().getY() + ")");
            return new Point(wire2.getStart().getX(), wire1.getStart().getY());
        }
        else {
            return null;
        }
    }


    public List<Component> verifyWirePlacement(Component component) {
        // returns list of wires that conflict or null if a non-wire conflict occured.
        ArrayList<Component> wireConflicts = new ArrayList<>();
        int nonWireConflicts = 0;

        Rectangle currentComponentRect = component.getComponentIcon().getFittingRect();

        List<ComponentIcon> existingComponents = grid.getComponents().stream()
                .map(comp -> comp.getComponentIcon()).collect(Collectors.toList());

        for(ComponentIcon comp : existingComponents) {
            if (currentComponentRect.getBoundsInParent().intersects(comp.getFittingRect().getBoundsInParent())) {
                Component conflictingComponent = grid.getComponent(comp.getID());
                if(conflictingComponent instanceof Wire) {
                    wireConflicts.add(conflictingComponent);
                }
                else {
                    comp.showError();
                    nonWireConflicts = nonWireConflicts + 1;
                }
            }
        }
        if (nonWireConflicts > 0) return null;
        return wireConflicts;
    }

    public boolean verifyPlacement(Component component) {
        // returns true if placement is valid, false if placement is invalid
        int conflicts = 0;

        Rectangle currentComponentRect = component.getComponentIcon().getFittingRect();

        List<ComponentIcon> existingComponents = grid.getComponents().stream()
                .map(comp -> comp.getComponentIcon()).collect(Collectors.toList());

        for(ComponentIcon comp : existingComponents) {
            if (currentComponentRect.getBoundsInParent().intersects(comp.getFittingRect().getBoundsInParent())) {
                comp.showError();
                conflicts = conflicts + 1;
            }
        }
        return conflicts == 0;
    }


    public Component verifySingleWirePosition(Component component) {
        Rectangle currentComponentRect = component.getComponentIcon().getFittingRect();

        List<ComponentIcon> existingComponents = grid.getComponents().stream()
                .map(comp -> comp.getComponentIcon()).collect(Collectors.toList());

        for(ComponentIcon compIcon : existingComponents) {
            if (currentComponentRect.getBoundsInParent().intersects(compIcon.getFittingRect().getBoundsInParent())) {
                Component conflictingComponent = grid.getComponent(compIcon.getID());
                return conflictingComponent;
            }
        }
        return null;
    }

    public void toggleComponent(String componentId) {
        Component component = grid.getComponent(componentId);

        if (component instanceof IToggleable) {
            ((IToggleable) component).toggle();
        }
    }


    private boolean isDevice(ComponentType componentType) {
        return switch (componentType) {
            case BREAKER_12KV, BREAKER_70KV, CUTOUT, JUMPER, SWITCH, TRANSFORMER -> true;
            default -> false;
        };
    }

    private boolean isSource(ComponentType componentType) {
        return switch (componentType) {
            case POWER_SOURCE, TURBINE -> true;
            default -> false;
        };
    }

}
