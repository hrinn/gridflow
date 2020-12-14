package construction;

import application.Globals;
import application.events.Event;
import application.events.EventManager;
import construction.canvas.GridCanvas;
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

    private GridCanvas canvas;
    private EventManager eventManager;
    private GridBuilder builderModel;
    private GhostManager ghostModel;

    private ToolType currentToolType = ToolType.SELECT;
    private ComponentType currentComponentType;
    private ComponentProperties properties;

    private final Image errorCursorImage = new Image("/resources/error_cursor.png");
    private final Cursor errorCursor = new ImageCursor(errorCursorImage, errorCursorImage.getWidth()/2,
            errorCursorImage.getHeight()/2);

    private WireExtendContext wireExtendContext = new WireExtendContext();

    public void initController(Grid grid, EventManager eventManager) {
        this.eventManager = eventManager;
        this.canvas = createCanvas();
        this.properties = new ComponentProperties();
        this.builderModel = new GridBuilder(grid, properties);
        this.ghostModel = new GhostManager(canvas, properties);
    }

    private GridCanvas createCanvas() {
        GridCanvas canvas = new GridCanvas();
        canvas.setTranslateX(-5350); // get this from application settings?
        canvas.setTranslateY(-2650);

        // component events
        canvas.setToggleComponentEventHandler(toggleComponentEventHandler);
        canvas.setEnterComponentHoverEventHandler(enterComponentHoverEventHandler);
        canvas.setExitComponentHoverEventHandler(exitComponentHoverEventHandler);

        // canvas events
        SceneGestures sceneGestures = new SceneGestures(canvas);

        // panning and scrolling
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getBeginPanEventHandler());
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnPanEventHandler());
        canvas.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        // component placement
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, placeComponentEventHandler);
        canvas.addEventFilter(MouseEvent.MOUSE_MOVED, ghostMoveEventHandler);
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, placeWireEventHandler);
        return canvas;
    }

    public GridCanvas getCanvas() {
        return canvas;
    }

    public void setCurrentToolType(ToolType currentToolType) {
        this.currentToolType = currentToolType;

        // If Placing a component, turn on ghosts
        if (currentToolType == ToolType.PLACE || currentToolType == ToolType.WIRE) {
            ghostModel.enableGhostIcon();
            canvas.setCursor(Cursor.NONE);
        } else {
            ghostModel.disableGhostIcon();
            canvas.setCursor(Cursor.DEFAULT);
        }
    }

    public void setCurrentComponentType(ComponentType componentType) {
        this.currentComponentType = componentType;
        ghostModel.setGhostIcon(componentType);
    }

    // Wire logic

    private final EventHandler<MouseEvent> placeWireEventHandler = event -> {
        if (!(currentToolType == ToolType.WIRE)) return;
        if (event.isSecondaryButtonDown()) return;

        if (wireExtendContext.placing) { // end placement
            wireExtendContext.placing = false;
            Point endPoint = getNearestCoordinate(event.getX(), event.getY());
            builderModel.placeWire(wireExtendContext.beginPoint, endPoint);
            ghostModel.setGhostIcon(currentComponentType);
            eventManager.sendEvent(Event.GridChanged);

        } else { // begin placement
            wireExtendContext.placing = true;
            wireExtendContext.beginPoint = getNearestCoordinate(event.getX(), event.getY());
        }
    };

    // Ghost Logic

    private final EventHandler<MouseEvent> enterComponentHoverEventHandler = event -> {
        if (!ghostModel.isGhostEnabled()) return;
        ghostModel.hideGhostIcon();
        canvas.setCursor(errorCursor);
        event.consume();
    };

    private final EventHandler<MouseEvent> exitComponentHoverEventHandler = event -> {
        if (!ghostModel.isGhostEnabled()) return;
        ghostModel.revealGhostIcon();
        canvas.setCursor(Cursor.NONE);
        event.consume();
    };

    private final EventHandler<MouseEvent> ghostMoveEventHandler = event -> {
        if (!ghostModel.isGhostEnabled()) return;
        Point coordPoint = getNearestCoordinate(event.getX(), event.getY());

        if (!wireExtendContext.placing) {
            ghostModel.updateGhostPosition(coordPoint);
        } else {
            ghostModel.extendGhostWire(wireExtendContext.beginPoint, coordPoint);
        }
    };

    // Construction Logic

    private final EventHandler<MouseEvent> toggleComponentEventHandler = event -> {
        event.consume();

        if (currentToolType != ToolType.SELECT) return;
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
