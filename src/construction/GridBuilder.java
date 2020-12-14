package construction;

import application.Globals;
import domain.Grid;
import domain.components.*;
import domain.geometry.Point;

public class GridBuilder {

    private Grid grid;
    private ComponentProperties properties;

    public GridBuilder(Grid grid) {
        this.grid = grid;
        properties = new ComponentProperties();
    }

    // This is what runs when a component is placed on the canvas standalone
    public void placeComponent(Point inputPosition, ComponentType componentType) {
        Point position = getNearestUnitCoordinate(inputPosition);
        if (isDevice(componentType)) {
            placeDevice(position, componentType);
        }
        else if (isSource(componentType)) {
            placeSource(position, componentType);
        }
        else if (componentType == ComponentType.WIRE) {
            placeWire(position); // this might need to be different
        }
    }

    public void placeDevice(Point position, ComponentType componentType) {
        // check for overlap conflicts! return if there is a conflict

        Device device = createDevice(position, componentType);
        if (device == null) return;

        Wire inWire = new Wire(position);
        device.connectInWire(inWire);
        inWire.connect(device);

        Wire outWire = new Wire(position.translate(0, device.getUnitHeight() * Globals.UNIT));
        device.connectOutWire(outWire);
        outWire.connect(device);

        grid.addComponents(device, inWire, outWire);
    }

    public Device createDevice(Point point, ComponentType componentType) {
        return switch (componentType) {
            case TRANSFORMER -> new Transformer(properties.getName(), point);
            case BREAKER -> new Breaker(properties.getName(), point, properties.getVoltage(), properties.getDefaultState());
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

                Wire outWire = new Wire(position.translate(0, powerSource.getUnitHeight() * Globals.UNIT));
                powerSource.connectWire(outWire);
                outWire.connect(powerSource);

                grid.addComponents(powerSource, outWire);
            }
            case TURBINE -> {
                Turbine turbine = new Turbine(properties.getName(), position, false);

                Wire topWire = new Wire(position);
                turbine.connectTopOutput(topWire);
                topWire.connect(turbine);

                Wire bottomWire = new Wire(position.translate(0, turbine.getUnitHeight() * Globals.UNIT));
                turbine.connectBottomOutput(bottomWire);
                bottomWire.connect(turbine);

                grid.addComponents(topWire, bottomWire, turbine);
            }
        }

    }

    public void placeWire(Point position) {

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
            case BREAKER, CUTOUT, JUMPER, SWITCH, TRANSFORMER -> true;
            default -> false;
        };
    }

    private boolean isSource(ComponentType componentType) {
        return switch (componentType) {
            case POWER_SOURCE, TURBINE -> true;
            default -> false;
        };
    }

    private Point getNearestUnitCoordinate(Point point) {
        double x = Math.round(point.getX()/Globals.UNIT) * Globals.UNIT;
        double y = Math.round(point.getY()/Globals.UNIT) * Globals.UNIT;
        return new Point(x, y);
    }

}
