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
        if (gridFlowEvent instanceof GridChangedEvent) {
            model.saveState();
        }
    }

    private final EventHandler<KeyEvent> undoEventHandler = event -> {
        if (!(event.getCode() == KeyCode.Z && event.isControlDown())) return;

        model.undo();
        eventManager.sendEvent(new StateRestoredEvent());

        event.consume();
    };

    private final EventHandler<KeyEvent> redoEventHandler = event -> {
        if (!(event.getCode() == KeyCode.Z && event.isControlDown() && event.isShiftDown())) return;

        model.redo();
        eventManager.sendEvent(new StateRestoredEvent());

        event.consume();
    };

    public EventHandler<KeyEvent> getUndoEventHandler() {
        return undoEventHandler;
    }

    public EventHandler<KeyEvent> getRedoEventHandler() {
        return redoEventHandler;
    }
}
