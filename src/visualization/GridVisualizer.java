package visualization;

import construction.canvas.GridCanvas;
import domain.Grid;
import domain.components.*;

public class GridVisualizer {

    private final Grid grid;
    private final GridCanvas canvas;

    public GridVisualizer(Grid grid, GridCanvas canvas) {
        this.grid = grid;
        this.canvas = canvas;
    }

    public void displayGrid() {
        canvas.clearComponents();
        // maybe set up the complicated component icon display stuff in here instead of component class
        for (Component component : grid.getComponents()) {
            canvas.addComponentIcon(component.getComponentIcon());
        }
    }

}
