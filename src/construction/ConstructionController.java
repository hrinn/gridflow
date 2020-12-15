package construction;

import application.Globals;
import application.events.Event;
import application.events.EventManager;
import construction.canvas.GridCanvas;
import construction.canvas.GridCanvasMaster;
import construction.canvas.SceneGestures;
import domain.Grid;
import domain.components.Wire;
import domain.geometry.Point;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class ConstructionController {

    private GridCanvasMaster canvasMaster;
    private EventManager eventManager;

    // Models
    private GridBuilder builderModel;
    private GhostManager ghostModel;
    private SelectionManager selectionModel;

    // Current tool, component, and properties
    private ToolType currentToolType = ToolType.INTERACT;
    private ComponentType currentComponentType;
    private ComponentProperties properties;

    // Cursors
    private static final Image errorCursorImage = new Image("/resources/error_cursor.png");
    private static final Cursor ERROR_CURSOR = new ImageCursor(errorCursorImage, errorCursorImage.getWidth()/2,
            errorCursorImage.getHeight()/2);
    private static final Cursor PLACE_CURSOR = Cursor.CROSSHAIR;

    // Wire Placing
    private WireExtendContext wireExtendContext = new WireExtendContext();

    public void initController(Grid grid, EventManager eventManager) {
        this.eventManager = eventManager;
        this.canvasMaster = new GridCanvasMaster();
        installEventHandlers();
        this.properties = new ComponentProperties();
        this.builderModel = new GridBuilder(grid, properties);
        this.ghostModel = new GhostManager(canvasMaster, properties);
        this.selectionModel = new SelectionManager();
    }

    private void installEventHandlers() {
        // Component events
        canvasMaster.setEnterComponentHoverEventHandler(enterComponentHoverEventHandler);
        canvasMaster.setExitComponentHoverEventHandler(exitComponentHoverEventHandler);
        canvasMaster.setToggleComponentEventHandler(toggleComponentEventHandler);

        // Canvas events
        canvasMaster.addCanvasEventHandler(MouseEvent.MOUSE_PRESSED, placeComponentEventHandler);
        canvasMaster.addCanvasEventFilter(MouseEvent.MOUSE_MOVED, ghostMoveEventHandler);
        canvasMaster.addCanvasEventFilter(MouseEvent.MOUSE_PRESSED, placeWireEventHandler);
    }

    public GridCanvasMaster getCanvasMaster() {
        return canvasMaster;
    }

    public void setCurrentToolType(ToolType currentToolType) {
        this.currentToolType = currentToolType;

        // If Placing a component, turn on ghosts
        if (currentToolType == ToolType.PLACE || currentToolType == ToolType.WIRE) {
            ghostModel.enableGhostIcon();
            canvasMaster.setCanvasCursor(PLACE_CURSOR);
        } else {
            ghostModel.disableGhostIcon();
            canvasMaster.setCanvasCursor(Cursor.DEFAULT);
        }
    }

    public void setCurrentComponentType(ComponentType componentType) {
        this.currentComponentType = componentType;
        ghostModel.setGhostIcon(componentType);
    }

    // Select logic

    private final EventHandler<MouseEvent> onSelectEventHandler = event -> {
        if (currentToolType != ToolType.SELECT) return;
        if (event.isSecondaryButtonDown()) return;

        String targetId = ((Node)event.getTarget()).getId();
        selectionModel.select(targetId);
    };

    // Wire logic

    private final EventHandler<MouseEvent> placeWireEventHandler = event -> {
        if (currentToolType != ToolType.WIRE) return;
        if (event.isSecondaryButtonDown()) return;

        // for implementing connecting/extending, try and reuse existing code
        if (wireExtendContext.placing) { // end placement
            wireExtendContext.placing = false;
            Point endPoint = getNearestCoordinate(event.getX(), event.getY());
            Point lockedEndPoint = getPerpendicularPosition(wireExtendContext.beginPoint, endPoint);
            builderModel.placeWire(wireExtendContext.beginPoint, lockedEndPoint);
            ghostModel.setGhostIcon(currentComponentType);
            eventManager.sendEvent(Event.GridChanged);

        } else { // begin placement
            wireExtendContext.placing = true;
            wireExtendContext.beginPoint = getNearestCoordinate(event.getX(), event.getY());
        }
    };

    private Point getPerpendicularPosition(Point start, Point end) {
        if (start.differenceX(end) > start.differenceY(end)) {
            return new Point(end.getX(), start.getY());
        } else {
            return new Point(start.getX(), end.getY());
        }
    }

    // Ghost Logic

    private final EventHandler<MouseEvent> enterComponentHoverEventHandler = event -> {
        if (!ghostModel.isGhostEnabled()) return;
        ghostModel.hideGhostIcon();
        canvasMaster.setCanvasCursor(ERROR_CURSOR);
        event.consume();
    };

    private final EventHandler<MouseEvent> exitComponentHoverEventHandler = event -> {
        if (!ghostModel.isGhostEnabled()) return;
        ghostModel.revealGhostIcon();
        canvasMaster.setCanvasCursor(PLACE_CURSOR);
        event.consume();
    };

    private final EventHandler<MouseEvent> ghostMoveEventHandler = event -> {
        if (!ghostModel.isGhostEnabled()) return;
        Point coordPoint = getNearestCoordinate(event.getX(), event.getY());

        if (!wireExtendContext.placing) {
            ghostModel.updateGhostPosition(coordPoint);
        } else {
            Point lockedCoordPoint = getPerpendicularPosition(wireExtendContext.beginPoint, coordPoint);
            ghostModel.extendGhostWire(wireExtendContext.beginPoint, lockedCoordPoint);
        }
    };

    // Construction Logic

    private final EventHandler<MouseEvent> toggleComponentEventHandler = event -> {
        event.consume();

        if (currentToolType != ToolType.INTERACT) return;
        if (event.isSecondaryButtonDown()) return;

        String targetId = ((Node)event.getTarget()).getId();
        builderModel.toggleComponent(targetId);
        eventManager.sendEvent(Event.GridChanged);
    };

    private final EventHandler<MouseEvent> placeComponentEventHandler = event -> {
        event.consume();

        if (currentToolType != ToolType.PLACE) return;
        if (event.isSecondaryButtonDown()) return;

        Point coordPoint = getNearestCoordinate(event.getX(), event.getY());
        builderModel.placeComponent(coordPoint, currentComponentType);
        eventManager.sendEvent(Event.GridChanged); // should only send this event if place comp returns true
    };

    private Point getNearestCoordinate(double x, double y) {
        double rx = Math.round(x/ Globals.UNIT) * Globals.UNIT;
        double ry = Math.round(y/Globals.UNIT) * Globals.UNIT;
        return new Point(rx, ry);
    }
}

class WireExtendContext {
    boolean placing = false;
    Point beginPoint;
}
