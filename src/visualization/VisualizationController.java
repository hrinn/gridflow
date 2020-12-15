package visualization;

import application.events.GridFlowEvent;
import application.events.GridFlowEventListener;
import construction.canvas.GridCanvasFacade;
import domain.Grid;

public class VisualizationController implements GridFlowEventListener {

    private GridVisualizer model;

    public VisualizationController(Grid grid, GridCanvasFacade canvasFacade) {
        this.model = new GridVisualizer(grid, canvasFacade);
    }

    public void initController(Grid grid, GridCanvasFacade canvasMaster) {
        this.model = new GridVisualizer(grid, canvasMaster);
    }

    public void handleEvent(GridFlowEvent gridFlowEvent) {
        if (gridFlowEvent == gridFlowEvent.GridEnergized) {
            model.displayGrid();
        }
    }
}
