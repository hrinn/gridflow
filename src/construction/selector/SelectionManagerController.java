package construction.selector;

import application.events.GridChangedEvent;
import application.events.GridFlowEventManager;
import application.events.SaveStateEvent;
import construction.buildMenu.BuildMenuData;
import construction.ToolType;
import construction.canvas.GridCanvasFacade;
import construction.history.GridMemento;
import construction.selector.observable.Observer;
import domain.Grid;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class SelectionManagerController {

    private SelectionManager model;
    private BuildMenuData buildMenuData;
    private GridFlowEventManager gridFlowEventManager;
    private boolean dragSelecting = false;
    private Grid grid;

    public SelectionManagerController(GridCanvasFacade canvasFacade, BuildMenuData buildMenuData, Grid grid,
                                      GridFlowEventManager gridFlowEventManager) {
        this.model = new SelectionManager(canvasFacade, grid);
        this.buildMenuData = buildMenuData;
        this.gridFlowEventManager = gridFlowEventManager;
        this.grid = grid;
    }

    public void addSelectedIDObserver(Observer<String> observer) {
        model.addObserver(observer);
    }

    public void buildMenuDataChanged() {
        model.deSelectAll();
    }

    private final EventHandler<MouseEvent> startSelectionEventHandler = event -> {
        if (!event.isPrimaryButtonDown()) return;
        if (buildMenuData.toolType != ToolType.SELECT) return;

        dragSelecting = true;
        model.beginSelection(event.getX(), event.getY());
        event.consume();
    };

    private final EventHandler<MouseEvent> expandSelectionEventHandler = event -> {
        if (!event.isPrimaryButtonDown()) return;
        if (buildMenuData.toolType != ToolType.SELECT) return;
        if (!dragSelecting) return;

        model.expandSelection(event.getX(), event.getY());
        event.consume();
    };

    private final EventHandler<MouseEvent> endSelectionEventHandler = event -> {
        if (buildMenuData.toolType != ToolType.SELECT) return;
        if (!dragSelecting) return;

        dragSelecting = false;
        model.endSelection();
        event.consume();
    };

    private final EventHandler<MouseEvent> selectSingleComponentHandler = event -> {
        if (!event.isPrimaryButtonDown()) return;
        if (buildMenuData.toolType != ToolType.SELECT) return;

        String targetID = ((Node)event.getTarget()).getId();
        if (event.isControlDown()) {
            model.continuousPointSelection(targetID);
        } else {
            model.pointSelection(targetID);
        }

        event.consume();
     };

    public void delete() {
        GridMemento preDeleteState = grid.makeSnapshot();
        int numDeleted = model.deleteSelectedItems();
        if (numDeleted != 0) {
            gridFlowEventManager.sendEvent(new SaveStateEvent(preDeleteState));
            gridFlowEventManager.sendEvent(new GridChangedEvent());
        }
    }

    public void selectAll() {
        model.selectAll();
    }

    public EventHandler<MouseEvent> getStartSelectionEventHandler() {
        return startSelectionEventHandler;
    }

    public EventHandler<MouseEvent> getExpandSelectionEventHandler() {
        return expandSelectionEventHandler;
    }

    public EventHandler<MouseEvent> getEndSelectionEventHandler() {
        return endSelectionEventHandler;
    }

    public EventHandler<MouseEvent> getSelectSingleComponentHandler() {
        return selectSingleComponentHandler;
    }
}
