package construction.history;

import application.events.GridFlowEvent;
import application.events.GridFlowEventListener;
import application.events.GridFlowEventManager;
import domain.Grid;

public class GridHistorianController implements GridFlowEventListener {

    private GridHistorian model;
    private Grid grid;
    private GridFlowEventManager eventManager;

    public GridHistorianController(Grid grid, GridFlowEventManager eventManager) {
        this.grid = grid;
        this.eventManager = eventManager;
        this.model = new GridHistorian();
    }

    @Override
    public void handleEvent(GridFlowEvent gridFlowEvent) {
        
    }
}
