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
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, placeComponentEventHandler);

        return canvas;
    }

    public GridCanvas getCanvas() {
        return canvas;
    }

    private final EventHandler<MouseEvent> toggleComponentEventHandler = event -> {
        if (event.isSecondaryButtonDown()) return;

        String targetId = ((Node)event.getTarget()).getId();
        model.toggleComponent(targetId);
        eventManager.sendEvent(Event.GridChanged);
    };

    private final EventHandler<MouseEvent> placeComponentEventHandler = event -> {
        if (event.isSecondaryButtonDown()) return;

        Point targetPosition = new Point(event.getX(), event.getY());
        model.placeComponent(targetPosition);
        eventManager.sendEvent(Event.GridChanged);
    };

}
