package construction;

import application.events.Event;
import application.events.EventManager;
import construction.canvas.GridCanvas;
import construction.canvas.SceneGestures;
import domain.Grid;
import domain.geometry.Point;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import visualization.componentIcons.ComponentIcon;

public class ConstructionController {

    private GridCanvas canvas;
    private EventManager eventManager;
    private GridBuilder builderModel;
    private GhostManager ghostModel;

    private ToolType currentToolType = ToolType.SELECT;
    private ComponentType currentComponentType;
    private ComponentProperties properties;

    public void initController(Grid grid, EventManager eventManager) {
        this.eventManager = eventManager;
        this.canvas = createCanvas();
        this.properties = new ComponentProperties();
        this.builderModel = new GridBuilder(grid, properties);
        this.ghostModel = new GhostManager(canvas, properties);
    }

    private GridCanvas createCanvas() {
        GridCanvas canvas = new GridCanvas(toggleComponentEventHandler);
        canvas.setTranslateX(-5350); // get this from application settings?
        canvas.setTranslateY(-2650);

        SceneGestures sceneGestures = new SceneGestures(canvas);
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getBeginPanEventHandler());
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnPanEventHandler());
        canvas.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, placeComponentEventHandler);
        canvas.addEventFilter(MouseEvent.MOUSE_MOVED, ghostMoveEventHandler);

        return canvas;
    }

    public GridCanvas getCanvas() {
        return canvas;
    }

    private final EventHandler<MouseEvent> ghostMoveEventHandler = event -> {
        if (!ghostModel.isGhostEnabled()) return;
        ghostModel.updateGhostPosition(event.getX(), event.getY());
    };

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

        Point targetPosition = new Point(event.getX(), event.getY());
        builderModel.placeComponent(targetPosition, currentComponentType);
        eventManager.sendEvent(Event.GridChanged); // should only send this event if place comp returns true
    };

    public void setCurrentToolType(ToolType currentToolType) {
        this.currentToolType = currentToolType;

        // If Placing a component, turn on ghosts
        if (currentToolType == ToolType.PLACE) {
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
}
