package visualization;

import application.events.Event;
import application.events.IEventListener;
import construction.canvas.GridCanvasFacade;
import domain.Grid;

public class VisualizationController implements IEventListener {

    private GridVisualizer model;

    public VisualizationController(Grid grid, GridCanvasFacade canvasFacade) {
        this.model = new GridVisualizer(grid, canvasFacade);
    }

    public void initController(Grid grid, GridCanvasFacade canvasMaster) {
        this.model = new GridVisualizer(grid, canvasMaster);
    }

    public void handleEvent(Event event) {
        if (event == Event.GridEnergized) {
            model.displayGrid();
        }
    }
}
