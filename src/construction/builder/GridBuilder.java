package construction.builder;

import construction.BoundType;
import construction.PropertiesData;
import construction.ComponentType;
import construction.canvas.GridCanvasFacade;
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

    // This is what runs when a component is placed on an existing component
    public boolean placeConnectedComponent(Point position, ComponentType componentType, String componentId) {
        Component component = grid.getComponent(componentId);

        if (component instanceof Wire) {
            if (isDevice(componentType)) {
                return placeConnectedDevice(position, componentType, (Wire) component);
            }
            else if (isSource(componentType)) {
                return placeConnectedSource(position, componentType, (Wire) component);
            }
        }

        return false;
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

    public boolean placeConnectedDevice(Point position, ComponentType componentType, Wire wire) {
        // check for overlap conflicts! return if there is a conflict

        Device device = createDevice(position, componentType);
        if (device == null) return false;
        device.setAngle(properties.getRotation());

        Wire inWire = wire;
        device.connectInWire(inWire);
        inWire.connect(device);

        Point outPoint = position.translate(0, device.getComponentIcon().getHeight());
        Wire outWire = new Wire(outPoint.rotate(properties.getRotation(), position));
        device.connectOutWire(outWire);
        outWire.connect(device);

        if(!verifyPlacement(device, BoundType.FittingRect)) return false;

        //TODO check other connection to see if multiple wires need connecting

        grid.addComponents(device, outWire);
        return true;
    }

    public boolean placeDevice(Point position, ComponentType componentType) {
        // check for overlap conflicts! return if there is a conflict

        Device device = createDevice(position, componentType);
        if (device == null) return false;
        device.setAngle(properties.getRotation());
        if(!verifyPlacement(device, BoundType.BoundingRect)) return false;

        Wire inWire = new Wire(position);
        device.connectInWire(inWire);
        inWire.connect(device);

        Point outPoint = position.translate(0, device.getComponentIcon().getHeight());
        Wire outWire = new Wire(outPoint.rotate(properties.getRotation(), position));
        device.connectOutWire(outWire);
        outWire.connect(device);


        grid.addComponents(device, inWire, outWire);
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

    public boolean placeConnectedSource(Point position, ComponentType componentType, Wire wire) {

        switch (componentType) {
            case POWER_SOURCE -> {
                PowerSource powerSource = new PowerSource(properties.getName(), position, false);
                powerSource.setAngle(properties.getRotation());

                Wire outWire = new Wire(position.translate(0, powerSource.getComponentIcon().getHeight())
                        .rotate(powerSource.getAngle(), position));
                powerSource.connectWire(outWire);
                outWire.connect(powerSource);

                if(!verifyPlacement(powerSource, BoundType.FittingRect)) return false;

                //TODO CONNECT SOURCE!!

                grid.addComponents(powerSource, outWire);
            }
            case TURBINE -> {
                Turbine turbine = new Turbine(properties.getName(), position, false);
                turbine.setAngle(properties.getRotation());

                Wire topWire = wire;
                turbine.connectTopOutput(topWire);
                topWire.connect(turbine);

                Wire bottomWire = new Wire(position.translate(0, turbine.getComponentIcon().getHeight())
                        .rotate(turbine.getAngle(), position));
                turbine.connectBottomOutput(bottomWire);
                bottomWire.connect(turbine);

                if(!verifyPlacement(turbine, BoundType.FittingRect)) return false;

                //TODO check other connection to see if multiple wires need connecting

                grid.addComponents(bottomWire, turbine);
            }
        }
        return true;
    }

    public boolean placeSource(Point position, ComponentType componentType) {

        switch (componentType) {
            case POWER_SOURCE -> {
                PowerSource powerSource = new PowerSource(properties.getName(), position, false);
                powerSource.setAngle(properties.getRotation());
                if(!verifyPlacement(powerSource, BoundType.BoundingRect)) return false;

                Wire outWire = new Wire(position);
                powerSource.connectWire(outWire);
                outWire.connect(powerSource);


                grid.addComponents(powerSource, outWire);
            }
            case TURBINE -> {
                Turbine turbine = new Turbine(properties.getName(), position, false);
                turbine.setAngle(properties.getRotation());
                if(!verifyPlacement(turbine, BoundType.BoundingRect)) return false;

                Wire topWire = new Wire(position);
                turbine.connectTopOutput(topWire);
                topWire.connect(turbine);

                Wire bottomWire = new Wire(position.translate(0, turbine.getComponentIcon().getHeight())
                        .rotate(turbine.getAngle(), position));
                turbine.connectBottomOutput(bottomWire);
                bottomWire.connect(turbine);


                grid.addComponents(topWire, bottomWire, turbine);
            }
        }
        return true;
    }

    public boolean placeWire(Point startPosition, Point endPosition) {
        Wire wire = new Wire(startPosition, endPosition);
        if (!verifyPlacement(wire, BoundType.BoundingRect)) return false;
        grid.addComponent(wire);
        return true;
    }

    // connect a component to an existing wire
    public void connectComponent() {

    }

    public boolean verifyPlacement(Component component, BoundType boundType) {
        // returns true if placement is valid, false if placement is invalid
        int conflicts = 0;

        Rectangle currentComponentRect;
        switch (boundType) {
            case FittingRect -> currentComponentRect = component.getComponentIcon().getFittingRect();
            case BoundingRect -> currentComponentRect = component.getComponentIcon().getBoundingRect();
            default -> {
                return false;
            }
        }

        List<ComponentIcon> existingComponents = grid.getComponents().stream()
                .map(comp -> comp.getComponentIcon()).collect(Collectors.toList());

        for(ComponentIcon comp : existingComponents) {
            if (currentComponentRect.getBoundsInParent().intersects(comp.getBoundingRect().getBoundsInParent())) {
                comp.showError();
                conflicts = conflicts + 1;
            }
        }
        return conflicts == 0;
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
