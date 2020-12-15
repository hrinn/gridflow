package visualization;

import construction.canvas.GridCanvas;
import construction.canvas.GridCanvasMaster;
import domain.Grid;
import domain.components.*;

public class GridVisualizer {

    private final Grid grid;
    private final GridCanvasMaster canvasMaster;

    public GridVisualizer(Grid grid, GridCanvasMaster canvasMaster) {
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
