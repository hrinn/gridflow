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

    public GridBuilder(Grid grid) {
        this.grid = grid;
    }

    // place a component standalone on the grid
    public void placeComponent(Point point) {
        Switch comp = new Switch("test", point, true);

        Wire inWire = new Wire(point);
        comp.connectInWire(inWire);
        inWire.connect(comp);

        Wire outWire = new Wire(point.translate(0, comp.unitHeight * Globals.UNIT));
        comp.connectOutWire(outWire);
        outWire.connect(comp);

        grid.addComponent(comp);
    }

    // connect a component to an existing component
    public void connectComponent() {

    }

    public void toggleComponent(String componentId) {
        Component component = grid.getComponent(componentId);

        if (component instanceof IToggleable) {
            ((IToggleable) component).toggle();
        }
    }

}
