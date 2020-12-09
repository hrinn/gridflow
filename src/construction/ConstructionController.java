package construction;

import application.canvas.PannableCanvas;
import domain.Grid;

public class ConstructionController {

    private Grid grid;

    private PannableCanvas canvas;

    public void initController(Grid grid, PannableCanvas canvas) {
        this.grid = grid;
        this.canvas = canvas;
    }

}
