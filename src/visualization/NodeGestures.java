package visualization;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
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

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {

            if (event.isSecondaryButtonDown()) return;

            Parent targetNode = ((Node)event.getTarget()).getParent();
            Component component = grid.getComponent(targetNode.getId());

            if (component instanceof IToggleable) {
                ((IToggleable)component).toggleState();
                eventManager.sendEvent(Event.GridChanged);

            }
        }

    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {

            // left mouse button => dragging
            if( !event.isPrimaryButtonDown())
                return;

            double scale = canvas.getScale();

            Node node = (Node) event.getSource();

            node.setTranslateX(nodeDragContext.translateAnchorX + (( event.getSceneX() - nodeDragContext.mouseAnchorX) / scale));
            node.setTranslateY(nodeDragContext.translateAnchorY + (( event.getSceneY() - nodeDragContext.mouseAnchorY) / scale));

            event.consume();

        }
    };
}
