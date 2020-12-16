package construction.selector;

import construction.BuildMenuData;
import construction.ToolType;
import construction.canvas.GridCanvasFacade;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class SelectionManagerController {

    private SelectionManager model;
    private BuildMenuData buildMenuData;

    public SelectionManagerController(GridCanvasFacade canvasFacade) {
        this.model = new SelectionManager(canvasFacade);
    }

    public void updateBuildMenuData(BuildMenuData buildMenuData) {
        this.buildMenuData = buildMenuData;
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
