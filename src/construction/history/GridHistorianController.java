package construction.history;

import application.events.*;
import domain.Grid;

public class GridHistorianController implements GridFlowEventListener {

    private GridHistorian model;
    private GridFlowEventManager eventManager;

    public GridHistorianController(Grid grid, GridFlowEventManager eventManager) {
        this.eventManager = eventManager;
        this.model = new GridHistorian(grid);
    }

    @Override
    public void handleEvent(GridFlowEvent gridFlowEvent) {
        if (gridFlowEvent instanceof SaveStateEvent) {
            GridMemento memento = ((SaveStateEvent)gridFlowEvent).getMemento();
            model.saveState(memento);
        }
    }

    public void undo() {
        model.undo();
        eventManager.sendEvent(new GridChangedEvent());
    }

    public void redo() {
        model.redo();
        eventManager.sendEvent(new GridChangedEvent());
    }
}
