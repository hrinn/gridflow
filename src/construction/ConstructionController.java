package construction;

import application.events.Event;
import application.events.EventManager;
import construction.canvas.GridCanvas;
import construction.canvas.SceneGestures;
import domain.Grid;
import domain.geometry.Point;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class ConstructionController {

    private GridCanvas canvas;
    private EventManager eventManager;
    private GridBuilder model;

    private ToolType currentToolType = ToolType.SELECT;
    private ComponentType currentComponentType;

    public void initController(Grid grid, EventManager eventManager) {
        this.eventManager = eventManager;
        this.canvas = createCanvas();
        this.model = new GridBuilder(grid);
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

        return canvas;
    }

    public GridCanvas getCanvas() {
        return canvas;
    }

    private final EventHandler<MouseEvent> toggleComponentEventHandler = event -> {
        event.consume();

        if (currentToolType != ToolType.SELECT) return;
        if (event.isSecondaryButtonDown()) return;

        String targetId = ((Node)event.getTarget()).getId();
        model.toggleComponent(targetId);
        eventManager.sendEvent(Event.GridChanged);
    };

    private final EventHandler<MouseEvent> placeComponentEventHandler = event -> {
        event.consume();

        if (!placingComponent(currentToolType)) return;
        if (event.isSecondaryButtonDown()) return;

        Point targetPosition = new Point(event.getX(), event.getY());
        model.placeDevice(targetPosition, currentComponentType);
        eventManager.sendEvent(Event.GridChanged);
    };

    private boolean placingComponent(ToolType toolType) {
        return toolType == ToolType.PLACE_DEVICE
                || toolType == ToolType.PLACE_WIRE
                || toolType == ToolType.PLACE_SOURCE;
    }


    public void setCurrentToolType(ToolType currentToolType) {
        this.currentToolType = currentToolType;
    }

    public void setCurrentComponentType(ComponentType currentComponentType) {
        this.currentComponentType = currentComponentType;
    }
}
