package construction.builder;

import application.Globals;
import construction.PropertiesData;
import construction.ComponentType;
import domain.Grid;
import domain.components.*;
import domain.geometry.Point;

public class GridBuilder {

    private Grid grid;
    private PropertiesData properties;

    public GridBuilder(Grid grid, PropertiesData properties) {
        this.grid = grid;
        this.properties = properties;
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

        Wire inWire = new Wire(position);
        device.connectInWire(inWire);
        inWire.connect(device);

        Point outPoint = position.translate(0, device.getDimensions().getHeight());
        Wire outWire = new Wire(outPoint.rotate(properties.getRotation(), position));
        device.connectOutWire(outWire);
        outWire.connect(device);
        device.setAngle(properties.getRotation());

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
