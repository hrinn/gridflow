package construction;

import application.events.EventManager;
import construction.builder.GridBuilderController;
import construction.canvas.GridCanvasFacade;
import construction.ghosts.GhostManagerController;
import construction.selector.SelectionManagerController;
import domain.Grid;
import javafx.scene.input.MouseEvent;

public class ConstructionController {

    private final GridCanvasFacade canvasFacade;
    private EventManager eventManager;

    // Sub Controllers
    private GridBuilderController gridBuilderController;
    private GhostManagerController ghostManagerController;
    private SelectionManagerController selectionManagerController;

    // User input data
    private PropertiesData properties;

    // Wire Placing
    private WireExtendContext wireExtendContext = new WireExtendContext();

    public ConstructionController(Grid grid, EventManager eventManager) {
        // shared objects
        this.eventManager = eventManager;
        this.canvasFacade = new GridCanvasFacade();
        this.properties = new PropertiesData(); // will be moved later

        // controllers
        gridBuilderController = new GridBuilderController(grid, properties, eventManager, wireExtendContext);
        ghostManagerController = new GhostManagerController(canvasFacade, properties, wireExtendContext);
        selectionManagerController = new SelectionManagerController();
        eventManager.addListener(ghostManagerController);

        installEventHandlers();
        setBuildMenuData(ToolType.INTERACT, ComponentType.NONE);
    }

    public GridCanvasFacade getCanvasFacade() {
        return canvasFacade;
    }

    public void setBuildMenuData(ToolType toolType, ComponentType componentType) {
        BuildMenuData buildData = new BuildMenuData();
        buildData.toolType = toolType;
        buildData.componentType = componentType;

        gridBuilderController.updateBuildMenuData(buildData);
        ghostManagerController.updateBuildMenuData(buildData);
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
    }
}
