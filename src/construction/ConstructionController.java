package construction;

import application.events.GridFlowEventManager;
import construction.builder.GridBuilderController;
import construction.canvas.GridCanvasFacade;
import construction.ghosts.GhostManagerController;
import construction.selector.SelectionManagerController;
import domain.Grid;
import javafx.scene.input.MouseEvent;

public class ConstructionController {

    private final GridCanvasFacade canvasFacade;
    private GridFlowEventManager gridFlowEventManager;

    // Sub Controllers
    private GridBuilderController gridBuilderController;
    private GhostManagerController ghostManagerController;
    private SelectionManagerController selectionManagerController;

    // UI Data
    private BuildMenuData buildMenuData;
    private PropertiesData propertiesData;

    // Wire Placing
    private WireExtendContext wireExtendContext = new WireExtendContext();

    public ConstructionController(Grid grid, GridFlowEventManager gridFlowEventManager) {
        // shared objects
        this.gridFlowEventManager = gridFlowEventManager;
        this.canvasFacade = new GridCanvasFacade();
        this.buildMenuData = new BuildMenuData();
        this.propertiesData = new PropertiesData();

        // controllers
        gridBuilderController = new GridBuilderController(grid, gridFlowEventManager, wireExtendContext, buildMenuData, propertiesData);
        ghostManagerController = new GhostManagerController(canvasFacade, wireExtendContext, buildMenuData, propertiesData);
        selectionManagerController = new SelectionManagerController(canvasFacade, buildMenuData);
        gridFlowEventManager.addListener(ghostManagerController);

        setPropertiesData(90);
        setBuildMenuData(ToolType.INTERACT, null);

        installEventHandlers();
    }

    public GridCanvasFacade getCanvasFacade() {
        return canvasFacade;
    }

    public void setBuildMenuData(ToolType toolType, ComponentType componentType) {
        if (toolType != null) buildMenuData.toolType = toolType;
        if (componentType != null) buildMenuData.componentType = componentType;

        // these run if the controllers need to react to build data changing
        ghostManagerController.buildMenuDataChanged();
        selectionManagerController.buildMenuDataChanged();
    }

    public void setPropertiesData(double rotation) {
        propertiesData.setRotation(rotation);

        gridBuilderController.propertiesDataChanged();
        ghostManagerController.propertiesDataChanged();
    }

    private void installEventHandlers() {
        // gets event handlers from the 3 sub controllers and installed them into the canvasFacade

        // builder events
        canvasFacade.setToggleComponentEventHandler(gridBuilderController.getToggleComponentEventHandler());
        canvasFacade.addCanvasEventHandler(MouseEvent.MOUSE_PRESSED, gridBuilderController.getPlaceComponentEventHandler());
        canvasFacade.addCanvasEventFilter(MouseEvent.MOUSE_PRESSED, gridBuilderController.getPlaceWireEventHandler());

        // ghost manager events
        canvasFacade.setEnterComponentHoverEventHandler(ghostManagerController.getEnterComponentHoverEventHandler());
        canvasFacade.setExitComponentHoverEventHandler(ghostManagerController.getExitComponentHoverEventHandler());
        canvasFacade.addCanvasEventFilter(MouseEvent.MOUSE_MOVED, ghostManagerController.getGhostMoveEventHandler());

        // selection events
        canvasFacade.addCanvasEventHandler(MouseEvent.MOUSE_PRESSED, selectionManagerController.getStartSelectionEventHandler());
        canvasFacade.addCanvasEventHandler(MouseEvent.MOUSE_DRAGGED, selectionManagerController.getExpandSelectionEventHandler());
        canvasFacade.addCanvasEventHandler(MouseEvent.MOUSE_RELEASED, selectionManagerController.getEndSelectionEventHandler());
        canvasFacade.setSelectSingleComponentHandler(selectionManagerController.getSelectSingleComponentHandler());
    }
}
