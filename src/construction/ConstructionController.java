package construction;

import application.events.Event;
import application.events.EventManager;
import construction.canvas.GridCanvas;
import construction.canvas.SceneGestures;
import domain.Grid;
import domain.components.Component;
import domain.components.IToggleable;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class ConstructionController {

    private Grid grid;
    private GridCanvas canvas;
    private EventManager eventManager;
    private GridBuilder model;

    public void initController(Grid grid, EventManager eventManager) {
        this.grid = grid;
        this.eventManager = eventManager;
        this.canvas = createCanvas();
        this.model = new GridBuilder();
    }

    private GridCanvas createCanvas() {
        GridCanvas canvas = new GridCanvas(toggleComponentEventHandler);
        canvas.setTranslateX(-5350); // get this from application settings?
        canvas.setTranslateY(-2650);

        SceneGestures sceneGestures = new SceneGestures(canvas);
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        canvas.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        return canvas;
    }

    public GridCanvas getCanvas() {
        return canvas;
    }

    private final EventHandler<MouseEvent> toggleComponentEventHandler = event -> {

        if (event.isSecondaryButtonDown()) return;

        Node target = (Node) event.getTarget();
        Component component = grid.getComponent(target.getId());

        if (component instanceof IToggleable) {
            ((IToggleable) component).toggle();
            eventManager.sendEvent(Event.GridChanged);
        }

    };

}
