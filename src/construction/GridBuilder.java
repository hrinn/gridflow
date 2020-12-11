package construction;

import application.Globals;
import domain.Grid;
import domain.components.*;
import domain.geometry.Point;

public class GridBuilder {

    private Grid grid;
    private ToolType currentTool = ToolType.PLACE;
    private ComponentType currentComponentType = ComponentType.BREAKER;
    private String componentName = "test";
    private Voltage currentVoltage = Voltage.KV12;
    private boolean closedByDefault = true;

    public GridBuilder(Grid grid) {
        this.grid = grid;
    }

    public ToolType getCurrentTool() {
        return currentTool;
    }

    public void setCurrentTool(ToolType currentTool) {
        this.currentTool = currentTool;
    }

    public ComponentType getCurrentComponentType() {
        return currentComponentType;
    }

    public void setCurrentComponentType(ComponentType currentComponentType) {
        this.currentComponentType = currentComponentType;
    }

    public String getComponentName() {
        return this.componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public boolean getClosedByDefault() {
        return this.closedByDefault;
    }

    public void setClosedByDefault(boolean closedByDefault) {
        this.closedByDefault = closedByDefault;
    }

    public Voltage getCurrentVoltage() {
        return this.currentVoltage;
    }

    public void setCurrentVoltage(Voltage currentVoltage) {
        this.currentVoltage = currentVoltage;
    }

    // place a component standalone on the grid
    public void placeComponent(Point inputPoint) {
        Point point = getNearestUnitCoordinate(inputPoint);
        // check for overlap conflicts! return if there is a conflict

        Device comp = createComponent(point);
        if(comp == null) {
            return;
        }

        Wire inWire = new Wire(point);
        comp.connectInWire(inWire);
        inWire.connect(comp);

        Wire outWire = new Wire(point.translate(0, comp.getUnitHeight() * Globals.UNIT));
        comp.connectOutWire(outWire);
        outWire.connect(comp);

        grid.addComponent(comp);
        grid.addComponent(inWire);
        grid.addComponent(outWire);
    }

    public Device createComponent(Point point) {
        switch(currentComponentType) {
            case TRANSFORMER:
                return new Transformer(componentName, point);

            //TODO address issue issue that non-devices do not have connectOutWire() and connectInWire()
            case POWERSOURCE:
                return null;

            case TURBINE:
                return null;

            case BREAKER:
                return new Breaker(componentName, point, currentVoltage, closedByDefault);

            case JUMPER:
                return new Jumper(componentName, point, closedByDefault);

            case CUTOUT:
                return new Cutout(componentName, point, closedByDefault);

            //TODO address how to place wires
            case WIRE:
                return null;

            case SWITCH:
                return new Switch(componentName, point, closedByDefault);

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
