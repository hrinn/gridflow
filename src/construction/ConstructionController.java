package construction;

import application.events.GridFlowEventManager;
import application.events.OpenAccountsEvent;
import application.events.ReLoginEvent;
import base.BaseMenuFunctions;
import construction.builder.GridBuilderController;
import construction.canvas.CanvasExpandController;
import construction.canvas.GridCanvasFacade;
import construction.ghosts.GhostManagerController;
import construction.history.GridHistorianController;
import construction.properties.PropertiesData;
import construction.properties.PropertiesManager;
import construction.properties.PropertiesObserver;
import construction.selector.SelectionManagerController;
import domain.Grid;
import domain.components.Component;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import security.Access;

import java.util.HashMap;
import java.util.Map;

public class ConstructionController implements BaseMenuFunctions, BuildMenuFunctions, PropertiesObserver {

    private GridCanvasFacade canvasFacade = null;
    private GridFlowEventManager gridFlowEventManager;
    private Stage stage;
    private Grid grid;

    // Security
    private Access access;

    // Sub Controllers
    private GridBuilderController gridBuilderController;
    private GhostManagerController ghostManagerController;
    private SelectionManagerController selectionManagerController;
    private GridHistorianController gridHistorianController;
    private CanvasExpandController canvasExpandController;

    // View Controllers
    private BuildMenuViewController buildMenuViewController;

    // UI Data
    private BuildMenuData buildMenuData;
    public PropertiesData propertiesData;
    public boolean isTyping;

    // Wire Placing and Association Placing
    // Used to share if a double click is in progress, and where the first click was if so
    private DoubleClickPlacementContext doubleClickContext = new DoubleClickPlacementContext();

    public ConstructionController(Grid grid, GridFlowEventManager gridFlowEventManager, Stage stage) {
        // shared objects
        this.gridFlowEventManager = gridFlowEventManager;
        this.canvasFacade = new GridCanvasFacade(stage.getScene());
        this.buildMenuData = new BuildMenuData();
        this.propertiesData = new PropertiesData();
        this.stage = stage;
        this.grid = grid;
        this.isTyping = false;

        // controllers
        gridBuilderController = new GridBuilderController(grid, gridFlowEventManager, doubleClickContext, buildMenuData,
                propertiesData, canvasFacade);
        ghostManagerController = new GhostManagerController(canvasFacade, doubleClickContext, buildMenuData);
        selectionManagerController = new SelectionManagerController(canvasFacade, buildMenuData, grid, gridFlowEventManager);
        gridHistorianController = new GridHistorianController(grid, gridFlowEventManager);
        canvasExpandController = new CanvasExpandController(stage.getScene(), canvasFacade);
        gridFlowEventManager.addListener(gridHistorianController);
        gridFlowEventManager.addListener(ghostManagerController);

        setBuildMenuData(ToolType.INTERACT, null);

        initializeComponentShortcutMap();

        installEventHandlers();
        PropertiesManager.attach(this);
    }

    public GridBuilderController getGridBuilderController () { return this.gridBuilderController; }

    public void setUserTyping(boolean typing) { this.isTyping = typing; }

    public GridCanvasFacade getCanvasFacade() {
        return canvasFacade;
    }

    public void setPermissions (Access access) { this.access = access; }


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

        ghostManagerController.propertiesDataChanged(rotationChanged, defaultStateChanged);
    }

    @Override
    public void updateProperties(PropertiesData properties){
        this.propertiesData = new PropertiesData(properties.getType(), properties.getID(), properties.getName(),
                properties.getDefaultState(), properties.getRotation(), properties.getNumSelected(),
                properties.getNamePos(), properties.getAssociation(), properties.getAssocLabel(),
                properties.getAssocSubLabel(), properties.getAssocAcronym());
    }

    public void notifyGhostController (boolean rotChanged, boolean stateChanged) {
        ghostManagerController.propertiesDataChanged(rotChanged, stateChanged);
    }

    private final EventHandler<KeyEvent> handleRKeyRotation = event -> {
        if (isTyping) return;
        if (event.getCode() != KeyCode.R) return;
        rotate(event.isControlDown());
        event.consume();
//        buildMenuViewController.setPropertiesWindow();
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

        // Toggle state, update ghost manager
        // If placing a new source component, they are on by default and can't be changed
        if ( !((propertiesData.getType() == ComponentType.POWER_SOURCE
                || propertiesData.getType() == ComponentType.TURBINE)
                &&  buildMenuData.toolType == ToolType.PLACE)) {

            propertiesData.setDefaultState(!propertiesData.getDefaultState());
            PropertiesManager.notifyObservers(propertiesData);
            ghostManagerController.propertiesDataChanged(false, true);
        }

        event.consume();

//        buildMenuViewController.setPropertiesWindow();
    };

    private final Map<KeyCode, Runnable> componentShortcutMap = new HashMap<>();

    private void initializeComponentShortcutMap() {
        componentShortcutMap.put(KeyCode.ESCAPE, () -> buildMenuViewController.selectInteractTool());
        componentShortcutMap.put(KeyCode.A, () -> buildMenuViewController.selectAssociationTool());
        componentShortcutMap.put(KeyCode.W, () -> buildMenuViewController.selectWireTool());
        componentShortcutMap.put(KeyCode.S, () -> buildMenuViewController.selectSelectTool());
        componentShortcutMap.put(KeyCode.DIGIT1, () -> buildMenuViewController.selectPowerSourceTool());
        componentShortcutMap.put(KeyCode.DIGIT2, () -> buildMenuViewController.selectTurbineTool());
        componentShortcutMap.put(KeyCode.DIGIT3, () -> buildMenuViewController.selectSwitchTool());
        componentShortcutMap.put(KeyCode.DIGIT4, () -> buildMenuViewController.selectBreaker12Tool());
        componentShortcutMap.put(KeyCode.DIGIT5, () -> buildMenuViewController.selectBreaker70Tool());
        componentShortcutMap.put(KeyCode.DIGIT6, () -> buildMenuViewController.selectTransformerTool());
        componentShortcutMap.put(KeyCode.DIGIT7, () -> buildMenuViewController.selectJumperTool());
        componentShortcutMap.put(KeyCode.DIGIT8, () -> buildMenuViewController.selectCutoutTool());
    }

    private final EventHandler<KeyEvent> handleComponentShortcut = event -> {
        if (isTyping) return;
        if (access == Access.VIEWER) return;

        // Runs the function associated with the keycode
        Runnable func = componentShortcutMap.getOrDefault(event.getCode(), null);
        if (func == null) return;
        func.run();

        event.consume();
    };

    // association event handlers
    // eats clicks on the association, to prevent associations from being placed inside the association
    private final EventHandler<MouseEvent> consumeAssociationClicksHandler = event -> {
        if (buildMenuData.toolType != ToolType.ASSOCIATION) return;
        event.consume();
    };

    private void rotate(boolean ccw) {
        double rotation;

        if (ccw) {
            rotation = (propertiesData.getRotation() == 0) ? 270 : propertiesData.getRotation() - 90;
        } else {
            rotation = (propertiesData.getRotation() == 270) ? 0 : propertiesData.getRotation() + 90;
        }

        // Update properties and ghost object
        propertiesData.setRotation(rotation);
        PropertiesManager.notifyObservers(propertiesData);
        ghostManagerController.propertiesDataChanged(true, false);
    }

    // Functions that implement button actions in the top menu
    @Override
    public void undo() {
        gridHistorianController.undo();
    }

    @Override
    public void redo() {
        gridHistorianController.redo();
    }

    @Override
    public void delete() {
        selectionManagerController.delete();
    }

    @Override
    public void selectAll() {
        selectionManagerController.selectAll();
    }

    @Override
    public void switchAccounts() {
        gridFlowEventManager.sendApplicationOnlyEvent(new ReLoginEvent());
    }

    @Override
    public void openAccountManager() {
        gridFlowEventManager.sendEvent(new OpenAccountsEvent());
    }

    @Override
    public void expandCanvas() {
        canvasExpandController.openAccountWindow();
    }

    @Override
    public void zoomToFit() {
        // Calculate middle position of grid
        double tx = 0;
        double ty = 0;
        double n = 0;
        for (Component comp : grid.getComponents()) {
            tx += comp.getPosition().getX();
            ty += comp.getPosition().getY();
            n++;
        }
        // Set middle position as pan
        canvasFacade.setCameraPos(tx/n, ty/n);
    }

    // gets event handlers from the sub controllers and installs them into the canvasFacade
    // event handlers are what respond to user inputs
    private void installEventHandlers() {

        // construction controller events
        stage.addEventFilter(KeyEvent.KEY_PRESSED, handleRKeyRotation);
        stage.addEventFilter(MouseEvent.MOUSE_PRESSED, handleMiddleMouseRotation);
        stage.addEventFilter(KeyEvent.KEY_PRESSED, handleToggleDefaultState);
        stage.addEventFilter(KeyEvent.KEY_PRESSED, handleComponentShortcut);

        // builder events
        canvasFacade.setToggleComponentEventHandler(gridBuilderController.getToggleComponentEventHandler());
        canvasFacade.setLockComponentEventHandler(gridBuilderController.getLockComponentEventHandler());
        canvasFacade.addCanvasEventHandler(MouseEvent.MOUSE_PRESSED, gridBuilderController.getPlaceComponentEventHandler());
        canvasFacade.addCanvasEventFilter(MouseEvent.MOUSE_PRESSED, gridBuilderController.getPlaceWireEventHandler());
        canvasFacade.addCanvasEventHandler(MouseEvent.MOUSE_PRESSED, gridBuilderController.getPlaceAssociationEventHandler());

        // ghost manager events
        canvasFacade.addCanvasEventFilter(MouseEvent.MOUSE_MOVED, ghostManagerController.getGhostMoveEventHandler());

        // selection events
        canvasFacade.addCanvasEventHandler(MouseEvent.MOUSE_PRESSED, selectionManagerController.getStartSelectionEventHandler());
        canvasFacade.addCanvasEventHandler(MouseEvent.MOUSE_DRAGGED, selectionManagerController.getExpandSelectionEventHandler());
        canvasFacade.addCanvasEventHandler(MouseEvent.MOUSE_RELEASED, selectionManagerController.getEndSelectionEventHandler());
        canvasFacade.setSelectSingleComponentHandler(selectionManagerController.getSelectSingleComponentHandler());

        // association events
        canvasFacade.setConsumeAssociationClicksHandler(consumeAssociationClicksHandler);

        // disables ghost when hovering over association
        canvasFacade.setBeginHoverAssociationHandler(ghostManagerController.getBeginHoverAssociationHandler());
        canvasFacade.setEndHoverAssociationHandler(ghostManagerController.getEndHoverAssociationHandler());

        // move text when dragging the text and show new cursor
        canvasFacade.setBeginAssociationTextDragHandler(gridBuilderController.getBeginAssociationTextDragEventHandler());
        canvasFacade.setDragAssociationTextHandler(gridBuilderController.getDragAssociationTextEventHandler());
        canvasFacade.setShowMoveCursorOnTextHoverHandler(gridBuilderController.getShowMoveCursorOnTextHoverHandler());
        canvasFacade.setShowDefaultCursorOnLeaveTextHoverHandler(gridBuilderController.getShowDefaultCursorOnLeaveTextHoverHandler());

        // resize association when dragging handles
        canvasFacade.setBeginResizeAssociationHandler(gridBuilderController.getBeginResizeAssociationEventHandler());
        canvasFacade.setResizeAssociationNWHandler(gridBuilderController.getResizeAssociationNWHandler());
        canvasFacade.setResizeAssociationSEHandler(gridBuilderController.getResizeAssociationSEHandler());
        canvasFacade.setResizeAssociationNEHandler(gridBuilderController.getResizeAssociationNEHandler());
        canvasFacade.setResizeAssociationSWHandler(gridBuilderController.getResizeAssociationSWHandler());
    }

    public void setBuildMenuViewController(BuildMenuViewController buildMenuViewController) {
        this.buildMenuViewController = buildMenuViewController;
    }
}
