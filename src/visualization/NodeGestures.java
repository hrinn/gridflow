package visualization;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import main.events.Event;
import main.events.EventManager;
import model.Grid;
import model.components.Component;
import model.components.IToggleable;

public class NodeGestures {

    private DragContext nodeDragContext = new DragContext();

    PannableCanvas canvas;
    EventManager eventManager; // should model interaction be moved somewhere else?
    Grid grid;

    public NodeGestures(PannableCanvas canvas, EventManager eventManager, Grid grid) {
        this.canvas = canvas;
        this.eventManager = eventManager;
        this.grid = grid;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    private final EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<>() {

        public void handle(MouseEvent event) {

            if (event.isSecondaryButtonDown()) return;

            Node target = (Node)event.getTarget();
            Component component = grid.getComponent(target.getId());

            if (component instanceof IToggleable) {
                ((IToggleable)component).toggleState();
                eventManager.sendEvent(Event.GridChanged);

            }
        }

    };
}
