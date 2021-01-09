package visualization;

import application.events.AssociationPlacedEvent;
import application.events.GridEnergizedEvent;
import application.events.GridFlowEvent;
import application.events.GridFlowEventListener;
import construction.canvas.GridCanvasFacade;
import domain.Grid;

public class VisualizationController implements GridFlowEventListener {

    private GridVisualizer model;

    public VisualizationController(Grid grid, GridCanvasFacade canvasFacade) {
        this.model = new GridVisualizer(grid, canvasFacade);
    }

    public void handleEvent(GridFlowEvent gridFlowEvent) {
        if (gridFlowEvent instanceof GridEnergizedEvent) {
            model.displayGrid();
        } else if (gridFlowEvent instanceof AssociationPlacedEvent) {
            model.displayAssociations();
        }
    }
}
