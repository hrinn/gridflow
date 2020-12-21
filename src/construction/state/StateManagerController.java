package construction.state;

import application.events.GridFlowEvent;
import application.events.GridFlowEventListener;
import application.events.GridFlowEventManager;
import domain.Grid;
import domain.Snapshot;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class StateManagerController implements GridFlowEventListener {

    private StateManager model;
    private Grid grid;
    private GridFlowEventManager gridFlowEventManager;

    public StateManagerController(Grid grid, GridFlowEventManager gridFlowEventManager) {
        this.model = new StateManager();
        this.grid = grid;
        this.gridFlowEventManager = gridFlowEventManager;
    }

    public void handleEvent(GridFlowEvent gridFlowEvent) {
        if (gridFlowEvent == GridFlowEvent.GridChanged) {
            model.loadGridState(grid);
        }
    }

    private final EventHandler<KeyEvent> undoRedoEventHandler = event -> {
        if (!(event.getCode() == KeyCode.Z && event.isControlDown())) return;

        Snapshot snapshot;

        if (event.isShiftDown()) { // redo
            snapshot = model.stepForward();
        } else { // undo
            snapshot = model.stepBack();
        }

        if (snapshot == null) return;
        grid.restore(snapshot);

        gridFlowEventManager.sendEvent(GridFlowEvent.StateRestored);
        event.consume();
    };

    public EventHandler<KeyEvent> getUndoRedoEventHandler() {
        return undoRedoEventHandler;
    }
}
