package visualization;

import construction.canvas.GridCanvas;
import application.events.Event;
import application.events.IEventListener;
import domain.Grid;

public class VisualizationController implements IEventListener {

   private GridVisualizer model;

    public void initController(Grid grid, GridCanvas canvas) {
        this.model = new GridVisualizer(grid, canvas);
    }

    public void handleEvent(Event event) {
        if (event == Event.GridEnergized) {
            model.displayGrid();
        }
    }
}
