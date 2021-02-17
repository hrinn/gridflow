package construction.history;

import application.events.*;
import domain.Grid;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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

    private final EventHandler<KeyEvent> undoRedoEventHandler = event -> {
        if (!(event.getCode() == KeyCode.Z && event.isControlDown())) return;

        if (event.isShiftDown()) {
            model.redo();
        } else {
            model.undo();
        }

        eventManager.sendEvent(new GridChangedEvent());

        event.consume();
    };

    public EventHandler<KeyEvent> getUndoRedoEventHandler() {
        return undoRedoEventHandler;
    }
}
