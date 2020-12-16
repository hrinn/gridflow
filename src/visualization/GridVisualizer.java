package visualization;

import construction.canvas.GridCanvasFacade;
import domain.Grid;
import domain.components.*;

public class GridVisualizer {

    private final Grid grid;
    private final GridCanvasFacade canvasMaster;

    public GridVisualizer(Grid grid, GridCanvasFacade canvasMaster) {
        this.grid = grid;
        this.canvasMaster = canvasMaster;
    }

    public void displayGrid() {
        canvasMaster.clearComponentGroups();
        // maybe set up the complicated component icon display stuff in here instead of component class
        for (Component component : grid.getComponents()) {
            canvasMaster.addComponentIcon(component.getComponentIcon());
        }
    }

}
