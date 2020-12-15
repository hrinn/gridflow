package visualization;

import construction.canvas.GridCanvas;
import application.events.Event;
import application.events.IEventListener;
import construction.canvas.GridCanvasMaster;
import domain.Grid;

public class VisualizationController implements IEventListener {

   private GridVisualizer model;

    public void initController(Grid grid, GridCanvasMaster canvasMaster) {
        this.model = new GridVisualizer(grid, canvasMaster);
    }

    public void handleEvent(Event event) {
        if (event == Event.GridEnergized) {
            model.displayGrid();
        }
    }
}
