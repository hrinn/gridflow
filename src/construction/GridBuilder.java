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

    // place a component standalone on the grid
    public void placeDevice(Point inputPoint, ComponentType componentType) {
        Point point = getNearestUnitCoordinate(inputPoint);
        // check for overlap conflicts! return if there is a conflict

        Device device = createDevice(point, componentType);
        if (device == null) return;

        Wire inWire = new Wire(point);
        device.connectInWire(inWire);
        inWire.connect(device);

        Wire outWire = new Wire(point.translate(0, device.getUnitHeight() * Globals.UNIT));
        device.connectOutWire(outWire);
        outWire.connect(device);

        grid.addComponent(device);
        grid.addComponent(inWire);
        grid.addComponent(outWire);
    }

    public Device createDevice(Point point, ComponentType componentType) {
        switch(componentType) {
            case TRANSFORMER:
                return new Transformer(properties.getName(), point);

            case BREAKER:
                return new Breaker(properties.getName(), point, properties.getVoltage(), properties.getDefaultState());

            case JUMPER:
                return new Jumper(properties.getName(), point, properties.getDefaultState());

            case CUTOUT:
                return new Cutout(properties.getName(), point, properties.getDefaultState());

            case SWITCH:
                return new Switch(properties.getName(), point, properties.getDefaultState());

            default:
                return null;
        }
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

    private Point getNearestUnitCoordinate(Point point) {
        double x = Math.round(point.getX()/Globals.UNIT) * Globals.UNIT;
        double y = Math.round(point.getY()/Globals.UNIT) * Globals.UNIT;
        return new Point(x, y);
    }

}
