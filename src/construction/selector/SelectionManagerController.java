package construction.selector;

import application.events.GridFlowEvent;
import application.events.GridFlowEventManager;
import construction.BuildMenuData;
import construction.PropertiesData;
import construction.ToolType;
import construction.canvas.GridCanvasFacade;
import domain.Grid;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class SelectionManagerController {

    private SelectionManager model;
    private BuildMenuData buildMenuData;
    private GridFlowEventManager gridFlowEventManager;

    public SelectionManagerController(GridCanvasFacade canvasFacade, BuildMenuData buildMenuData, Grid grid,
                                      GridFlowEventManager gridFlowEventManager) {
        this.model = new SelectionManager(canvasFacade, grid);
        this.buildMenuData = buildMenuData;
        this.gridFlowEventManager = gridFlowEventManager;
    }

    public void buildMenuDataChanged() {
        model.deSelectAll();
    }

    private final EventHandler<MouseEvent> startSelectionEventHandler = event -> {
        if (event.isSecondaryButtonDown()) return;
        if (buildMenuData.toolType != ToolType.SELECT) return;

        model.beginSelection(event.getX(), event.getY());
        event.consume();
    };

    private final EventHandler<MouseEvent> expandSelectionEventHandler = event -> {
        if (event.isSecondaryButtonDown()) return;
        if (buildMenuData.toolType != ToolType.SELECT) return;

        model.expandSelection(event.getX(), event.getY());
        event.consume();
    };

    private final EventHandler<MouseEvent> endSelectionEventHandler = event -> {
        if (event.isSecondaryButtonDown()) return;
        if (buildMenuData.toolType != ToolType.SELECT) return;

        model.endSelection();
        event.consume();
    };

    private final EventHandler<MouseEvent> selectSingleComponentHandler = event -> {
        if (event.isSecondaryButtonDown()) return;
        if (buildMenuData.toolType != ToolType.SELECT) return;

        String targetID = ((Node)event.getTarget()).getId();
        model.pointSelection(targetID);

        event.consume();
     };

    private final EventHandler<KeyEvent> deleteHandler = event -> {
        if (event.getCode() != KeyCode.DELETE) return;

        model.deleteSelectedComponents();
        gridFlowEventManager.sendEvent(GridFlowEvent.GridChanged);
        event.consume();
    };

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

    public EventHandler<KeyEvent> getDeleteHandler() {
        return deleteHandler;
    }
}
