package visualization;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.events.Event;
import main.events.EventManager;
import main.events.IEventListener;
import model.Grid;
import model.components.*;

public class GraphVisualizer implements IEventListener {

    private PannableCanvas canvas = new PannableCanvas();
    private Grid grid;
    private EventManager eventManager;

    public GraphVisualizer(Grid grid, EventManager eventManager) {
        this.grid = grid;
        this.eventManager = eventManager;
        eventManager.addListener(this);

        NodeGestures nodeGestures = new NodeGestures(canvas);
        SceneGestures sceneGestures = new SceneGestures(canvas);
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        canvas.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        //EventHandler<MouseEvent> nodeClickedHandler = this::handleNodeClicked;
        //nodes.addEventFilter(MouseEvent.MOUSE_CLICKED, nodeClickedHandler);
    }

    public void handleEvent(Event event) {
        if (event == Event.GridEnergized) {
            displayGrid();
        }
    }

    private void handleNodeClicked(MouseEvent mouseEvent) {
        Parent targetNode = ((Node)mouseEvent.getTarget()).getParent();
        targetNode.setScaleX(100);
        targetNode.setScaleY(100);

        Component component = grid.getComponent(targetNode.getId());

        if (component instanceof IToggleable) {
            ((IToggleable)component).toggleState();
            eventManager.sendEvent(Event.GridChanged);

        }
    }


    public PannableCanvas getGridCanvas() {
        return canvas;
    }

    private void addTextElement(Group node, Text text) {
        node.getChildren().add(text);
    }

    private void addNodeToCanvas(Node node) {
        canvas.getChildren().add(node);
    }

    public void displayGrid() {
        clearGraph();

        // draw connections
        for (Wire wire : grid.getWires()) {
            for (Component connection : wire.getConnections()) {
                addConnection(wire, connection);
            }
        }

        // draw components
        for (Component component : grid.getComponents()) {
            addComponent(component);
        }
    }

    public void addConnection(Component c1, Component c2) {
        Line line = new Line();

        line.setStartX(c1.getPosition().getX());
        line.setStartY(c1.getPosition().getY());

        line.setEndX(c2.getPosition().getX());
        line.setEndY(c2.getPosition().getY());

        addNodeToCanvas(line);
    }

    private void addComponent(Component component) {
        // upperArc definition
        Arc upperArc = new Arc();
        upperArc.setCenterX(component.getPosition().getX());
        upperArc.setCenterY(component.getPosition().getY());
        upperArc.setLength(180);
        upperArc.setRadiusX(30);
        upperArc.setRadiusY(20);
        upperArc.setStartAngle(0);
        upperArc.setType(ArcType.OPEN);

        // lowerArc definition
        Arc lowerArc = new Arc();
        lowerArc.setCenterX(component.getPosition().getX());
        lowerArc.setCenterY(component.getPosition().getY());
        lowerArc.setLength(180);
        lowerArc.setRadiusX(30);
        lowerArc.setRadiusY(20);
        lowerArc.setStartAngle(180);
        lowerArc.setType(ArcType.OPEN);

        // set fill different based on type (this is bad code and will work differently later)
        if (component instanceof Wire) {
            Wire wire = (Wire)component;
            upperArc.setStroke(wire.isEnergized() ? Color.YELLOW : Color.BLACK);
            lowerArc.setStroke(wire.isEnergized() ? Color.YELLOW : Color.BLACK);

        } else if (component instanceof Device) {
            Device device = (Device)component;

            if (device.isInWireEnergized() && device.isOutWireEnergized()) {
                upperArc.setStroke(Color.YELLOW);
                lowerArc.setStroke(Color.YELLOW);
            } else if (!device.isInWireEnergized() && !device.isOutWireEnergized()) {
                upperArc.setStroke(Color.BLACK);
                lowerArc.setStroke(Color.BLACK);
            } else  {
                // partially energized
                upperArc.setStroke(device.isInWireEnergized() ? Color.YELLOW : Color.BLACK);
                lowerArc.setStroke(device.isOutWireEnergized() ? Color.YELLOW : Color.BLACK);
            }

        } else { // its a source
            Source source = (Source)component;
            upperArc.setStroke(source.getState() ? Color.YELLOW : Color.BLACK);
            lowerArc.setStroke(source.getState() ? Color.YELLOW : Color.BLACK);
        }
        upperArc.setFill(Color.WHITE);
        lowerArc.setFill(Color.WHITE);

        Text name = new Text();
        name.setText(component.getName());
        name.setX(upperArc.getCenterX());
        name.setY(upperArc.getCenterY());
        name.setTextAlignment(TextAlignment.RIGHT);

        Group node = new Group(upperArc, lowerArc, name);
        node.setId(component.getId().toString());
        addNodeToCanvas(node);

        // only display if it can be toggled
        if (component instanceof IToggleable) {
            Text state = new Text();
            state.setText(((IToggleable) component).getState() ? "closed" : "open");
            state.setX(upperArc.getCenterX());
            state.setY(upperArc.getCenterY() + 10);
            state.setTextAlignment(TextAlignment.LEFT);
            addTextElement(node, state);
        }

    }

    private void clearGraph() {
        canvas.getChildren().clear();
    }

}
