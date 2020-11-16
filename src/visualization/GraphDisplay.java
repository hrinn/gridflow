package visualization;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
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

public class GraphDisplay implements IEventListener {

    private Group nodes;
    private Group edges;
    private Group graphRoot;
    private Grid grid;
    private EventManager eventManager;

    public GraphDisplay(Grid grid, EventManager eventManager) {
        nodes = new Group();
        edges = new Group();
        graphRoot = new Group(edges, nodes);
        this.grid = grid;
        this.eventManager = eventManager;

        EventHandler<MouseEvent> nodeClickedHandler = this::handleNodeClicked;
        nodes.addEventFilter(MouseEvent.MOUSE_CLICKED, nodeClickedHandler);
    }

    public void handleEvent(Event event) {
        if (event == Event.GridEnergized) {
            displayGrid();
        }
    }

    private void handleNodeClicked(MouseEvent mouseEvent) {
        Parent targetNode = ((Node)mouseEvent.getTarget()).getParent();

        Component component = grid.getComponent(targetNode.getId());

        if (component instanceof IToggleable) {
            ((IToggleable)component).toggleState();
            eventManager.sendEvent(Event.GridChanged);

        }
    }


    public Group getGraphRoot() {
        return graphRoot;
    }

    private void addTextElement(Group node, Text text) {
        node.getChildren().add(text);
    }

    private void addNodeElement(Group node) {
        nodes.getChildren().add(node);
    }

    private void addEdgeElement(Line edge) {
        edges.getChildren().add(edge);
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

        addEdgeElement(line);
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
        addNodeElement(node);

        // only display if it can be toggled
        if (component instanceof IToggleable) {
            Text state = new Text();
            state.setText(((IToggleable) component).getState() ? "true" : "false");
            state.setX(upperArc.getCenterX());
            state.setY(upperArc.getCenterY() + 10);
            state.setTextAlignment(TextAlignment.LEFT);
            addTextElement(node, state);
        }

    }

    private void clearGraph() {
        nodes.getChildren().clear();
        edges.getChildren().clear();
    }

}
