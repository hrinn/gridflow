package construction;

import application.Globals;
import domain.Grid;
import domain.components.Component;
import domain.components.IToggleable;
import domain.components.Switch;
import domain.components.Wire;
import domain.geometry.Point;

public class GridBuilder {

    private Grid grid;
    private ToolType currentTool = ToolType.PLACE;
    private ComponentType currentComponentType = ComponentType.SWITCH;

    public GridBuilder(Grid grid) {
        this.grid = grid;
    }

    public ToolType getCurrentTool() {
        return currentTool;
    }

    public ComponentType getCurrentComponentType() {
        return currentComponentType;
    }

    // place a component standalone on the grid
    public void placeComponent(Point inputPoint) {
        Point point = getNearestUnitCoordinate(inputPoint);
        // check for overlap conflicts! return if there is a conflict

        Switch comp = new Switch("test", point, true);

        Wire inWire = new Wire(point);
        comp.connectInWire(inWire);
        inWire.connect(comp);

        Wire outWire = new Wire(point.translate(0, comp.unitHeight * Globals.UNIT));
        comp.connectOutWire(outWire);
        outWire.connect(comp);

        grid.addComponent(comp);
        grid.addComponent(inWire);
        grid.addComponent(outWire);
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
