package construction;

import application.events.GridFlowEventManager;
import construction.builder.GridBuilderController;
import construction.canvas.GridCanvasFacade;
import construction.ghosts.GhostManagerController;
import construction.selector.SelectionManagerController;
import domain.Grid;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ConstructionController implements PropertiesObserver {

    private final GridCanvasFacade canvasFacade;
    private GridFlowEventManager gridFlowEventManager;
    private Stage stage;

    // Sub Controllers
    private GridBuilderController gridBuilderController;
    private GhostManagerController ghostManagerController;
    private SelectionManagerController selectionManagerController;

    // UI Data
    private BuildMenuData buildMenuData;
    public PropertiesData propertiesData;
    public boolean isTyping;

    // Wire Placing
    private WireExtendContext wireExtendContext = new WireExtendContext();

    public ConstructionController(Grid grid, GridFlowEventManager gridFlowEventManager, Stage stage) {
        // shared objects
        this.gridFlowEventManager = gridFlowEventManager;
        this.canvasFacade = new GridCanvasFacade();
        this.buildMenuData = new BuildMenuData();
        this.propertiesData = new PropertiesData();
        this.stage = stage;
        this.isTyping = false;

        // controllers
        gridBuilderController = new GridBuilderController(grid, gridFlowEventManager, wireExtendContext, buildMenuData, propertiesData);
        ghostManagerController = new GhostManagerController(canvasFacade, wireExtendContext, buildMenuData, propertiesData);
        selectionManagerController = new SelectionManagerController(canvasFacade, buildMenuData, grid, gridFlowEventManager);
        gridFlowEventManager.addListener(ghostManagerController);

        //setPropertiesData(0, true);
        setBuildMenuData(ToolType.INTERACT, null);

        installEventHandlers();
        PropertiesManager.attach(this);
    }

    public void setUserTyping(boolean typing) { this.isTyping = typing; }

    public GridCanvasFacade getCanvasFacade() {
        return canvasFacade;
    }

    public void setBuildMenuData(ToolType toolType, ComponentType componentType) {
        if (toolType != null) buildMenuData.toolType = toolType;
        if (componentType != null) buildMenuData.componentType = componentType;

        // these run if the controllers need to react to build data changing
        ghostManagerController.buildMenuDataChanged();
        selectionManagerController.buildMenuDataChanged();
        gridBuilderController.buildDataChanged();
    }

    public void setPropertiesData(double rotation, boolean defaultState) {
        boolean rotationChanged = rotation != propertiesData.getRotation();
        boolean defaultStateChanged = defaultState != propertiesData.getDefaultState();

        propertiesData.setRotation(rotation);
        propertiesData.setDefaultState(defaultState);

        // Use this call to gridbuilder to pass back the necessary data (dead call)
        gridBuilderController.propertiesDataChanged();
        ghostManagerController.propertiesDataChanged(rotationChanged, defaultStateChanged);
    }

    @Override
    public void updateProperties(PropertiesData properties){
        this.propertiesData = properties;
    }


    private final EventHandler<KeyEvent> handleRKeyRotation = event -> {
        if (isTyping) return;
        if (event.getCode() != KeyCode.R) return;
        rotate(event.isControlDown());
        event.consume();
    };

    private final EventHandler<MouseEvent> handleMiddleMouseRotation = event -> {
        if (!event.isMiddleButtonDown()) return;
        rotate(false);
        event.consume();
    };

    private final EventHandler<KeyEvent> handleToggleDefaultState = event -> {
        if (isTyping) return;
        if (event.getCode() != KeyCode.E) return;
        if (buildMenuData.toolType != ToolType.PLACE) return;
        setPropertiesData(propertiesData.getRotation(), !propertiesData.getDefaultState());
        event.consume();
    };

    private final EventHandler<KeyEvent> handleEscapeTool = event -> {
        if (event.getCode() != KeyCode.ESCAPE) return;
        if (buildMenuData.toolType == ToolType.INTERACT) return;
        setBuildMenuData(ToolType.INTERACT, buildMenuData.componentType);
        event.consume();
    };

    private void rotate(boolean ccw) {
        double rotation;

        if (ccw) {
            rotation = (propertiesData.getRotation() == 0) ? 270 : propertiesData.getRotation() - 90;
        } else {
            rotation = (propertiesData.getRotation() == 270) ? 0 : propertiesData.getRotation() + 90;
        }

        setPropertiesData(rotation, propertiesData.getDefaultState());
    }

    private void installEventHandlers() {
        // gets event handlers from the 3 sub controllers and installed them into the canvasFacade

        // construction controller events
        stage.addEventFilter(KeyEvent.KEY_PRESSED, handleRKeyRotation);
        stage.addEventFilter(MouseEvent.MOUSE_PRESSED, handleMiddleMouseRotation);
        stage.addEventFilter(KeyEvent.KEY_PRESSED, handleToggleDefaultState);
        stage.addEventFilter(KeyEvent.KEY_PRESSED, handleEscapeTool);

        // builder events
        canvasFacade.setToggleComponentEventHandler(gridBuilderController.getToggleComponentEventHandler());
        canvasFacade.addCanvasEventHandler(MouseEvent.MOUSE_PRESSED, gridBuilderController.getPlaceComponentEventHandler());
        canvasFacade.addCanvasEventFilter(MouseEvent.MOUSE_PRESSED, gridBuilderController.getPlaceWireEventHandler());

        // ghost manager events
        canvasFacade.addCanvasEventFilter(MouseEvent.MOUSE_MOVED, ghostManagerController.getGhostMoveEventHandler());

        // selection events
        canvasFacade.addCanvasEventHandler(MouseEvent.MOUSE_PRESSED, selectionManagerController.getStartSelectionEventHandler());
        canvasFacade.addCanvasEventHandler(MouseEvent.MOUSE_DRAGGED, selectionManagerController.getExpandSelectionEventHandler());
        canvasFacade.addCanvasEventHandler(MouseEvent.MOUSE_RELEASED, selectionManagerController.getEndSelectionEventHandler());
        canvasFacade.setSelectSingleComponentHandler(selectionManagerController.getSelectSingleComponentHandler());
        stage.addEventFilter(KeyEvent.KEY_PRESSED, selectionManagerController.getDeleteHandler());
    }
}
