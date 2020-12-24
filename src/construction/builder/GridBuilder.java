package construction.builder;

import application.Globals;
import construction.PropertiesData;
import construction.ComponentType;
import construction.canvas.GridCanvasFacade;
import domain.Grid;
import domain.components.*;
import domain.geometry.Point;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.css.Rect;
import visualization.componentIcons.ComponentIcon;

import java.util.ArrayList;
import java.util.List;

public class GridBuilder {

    private Grid grid;
    private PropertiesData properties;
    private GridCanvasFacade canvasFacade;

    public GridBuilder(Grid grid, PropertiesData properties, GridCanvasFacade canvasFacade) {
        this.grid = grid;
        this.properties = properties;
        this.canvasFacade = canvasFacade;
    }

    // This is what runs when a component is placed on the canvas standalone
    public void placeComponent(Point position, ComponentType componentType) {
        if (isDevice(componentType)) {
            placeDevice(position, componentType);
        }
        else if (isSource(componentType)) {
            placeSource(position, componentType);
        }
    }

    public void placeDevice(Point position, ComponentType componentType) {
        // check for overlap conflicts! return if there is a conflict

        Device device = createDevice(position, componentType);
        if (device == null) return;
        device.setAngle(properties.getRotation());

        Wire inWire = new Wire(position);
        device.connectInWire(inWire);
        inWire.connect(device);

        Point outPoint = position.translate(0, device.getDimensions().getHeight());
        Wire outWire = new Wire(outPoint.rotate(properties.getRotation(), position));
        device.connectOutWire(outWire);
        outWire.connect(device);

        if(!verifyPlacement(device)) {
            System.out.println("Failure to place Device");
            return;
        }

        grid.addComponents(device, inWire, outWire);
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

    public void placeSource(Point position, ComponentType componentType) {

        switch (componentType) {
            case POWER_SOURCE -> {
                PowerSource powerSource = new PowerSource(properties.getName(), position, false);
                powerSource.setAngle(properties.getRotation());

                Wire outWire = new Wire(position.translate(0, powerSource.getDimensions().getHeight())
                    .rotate(powerSource.getAngle(), position));
                powerSource.connectWire(outWire);
                outWire.connect(powerSource);

                if(!verifyPlacement(powerSource)) {
                    System.out.println("Failure to place PowerSource");
                    return;
                }

                grid.addComponents(powerSource, outWire);
            }
            case TURBINE -> {
                Turbine turbine = new Turbine(properties.getName(), position, false);
                turbine.setAngle(properties.getRotation());

                Wire topWire = new Wire(position);
                turbine.connectTopOutput(topWire);
                topWire.connect(turbine);

                Wire bottomWire = new Wire(position.translate(0, turbine.getDimensions().getHeight())
                        .rotate(turbine.getAngle(), position));
                turbine.connectBottomOutput(bottomWire);
                bottomWire.connect(turbine);

                if(!verifyPlacement(turbine)) {
                    System.out.println("Failure to place Turbine");
                    return;
                }

                grid.addComponents(topWire, bottomWire, turbine);
            }
        }

    }

    public void placeWire(Point startPosition, Point endPosition) {
        Wire wire = new Wire(startPosition, endPosition);
        grid.addComponent(wire);
    }

    // connect a component to an existing wire
    public void connectComponent() {

    }

    public boolean verifyPlacement(Component component) {
        // returns true if placement is valid, false if placement is invalid
        Node currentComponentRect = component.getComponentIcon().getComponentNode();
        List<Rectangle> existingBoundingRects = canvasFacade.getAllBoundingRects();

        for(Rectangle gridRect : existingBoundingRects) {
            if (currentComponentRect.getBoundsInParent().intersects(gridRect.getBoundsInParent())) {
                return false;
            }
        }
        return true;
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
