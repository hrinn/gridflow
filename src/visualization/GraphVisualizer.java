package visualization;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.events.Event;
import main.events.EventManager;
import main.events.IEventListener;
import model.Grid;
import model.components.*;
import model.geometry.Point;

public class GraphVisualizer implements IEventListener {

    private PannableCanvas canvas = new PannableCanvas(12000, 6000);
    private Grid grid;
    private EventManager eventManager;
    private NodeGestures nodeGestures;

    public GraphVisualizer(Grid grid, EventManager eventManager) {
        this.grid = grid;
        this.eventManager = eventManager;
        eventManager.addListener(this);
        canvas.setTranslateX(-5350);
        canvas.setTranslateY(-2650);

        nodeGestures = new NodeGestures(canvas, eventManager, grid);
        SceneGestures sceneGestures = new SceneGestures(canvas);
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        canvas.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        testDrawComps();
    }

    private void testDrawComps() {
        Point center = new Point(5350, 2650);

        DeviceIcon switchIcon = ComponentIconCreator.getSwitchIcon(center);
        switchIcon.setDeviceEnergyStates(true, false);
        addNodeToCanvas(switchIcon);

        DeviceIcon breakerIcon = ComponentIconCreator.get70KVBreakerIcon(center.translate(40, 0));
        breakerIcon.setDeviceEnergyStates(true, true);
        addNodeToCanvas(breakerIcon);

        DeviceIcon breakerIcon2 = ComponentIconCreator.get12KVBreakerIcon(center.translate(80, -10));
        breakerIcon2.setDeviceEnergyStates(true, false);
        addNodeToCanvas(breakerIcon2);

        DeviceIcon xformIcon = ComponentIconCreator.getTransformerIcon(center.translate(130, 0));
        xformIcon.setDeviceEnergyStates(true, true);
        addNodeToCanvas(xformIcon);

        DeviceIcon jumperIcon = ComponentIconCreator.getJumperIcon(center.translate(170, 0), false);
        jumperIcon.setDeviceEnergyStates(false, true);
        addNodeToCanvas(jumperIcon);

        DeviceIcon cutoutIcon = ComponentIconCreator.getCutoutIcon(center.translate(210, 0), false);
        cutoutIcon.setDeviceEnergyStates(false, false);
        addNodeToCanvas(cutoutIcon);
    }

    public void handleEvent(Event event) {
        if (event == Event.GridEnergized) {
            //displayGrid();
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
        node.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
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
        Point c1CanvasPosition = gridPositionToCanvasPosition(c1.getPosition());
        Point c2CanvasPosition = gridPositionToCanvasPosition(c2.getPosition());

        line.setStartX(c1CanvasPosition.getX());
        line.setStartY(c1CanvasPosition.getY());

        line.setEndX(c2CanvasPosition.getX());
        line.setEndY(c2CanvasPosition.getY());
        addNodeToCanvas(line);
    }

    private Point gridPositionToCanvasPosition(Point gridPosition) {
        return gridPosition.scale(100).translate(canvas.getWidth()/2, canvas.getHeight()/2);
    }

    private void addComponent(Component component) {
        // Component position to canvas position
        Point canvasPosition = gridPositionToCanvasPosition(component.getPosition());

        // upperArc definition
        Arc upperArc = new Arc();
        upperArc.setCenterX(canvasPosition.getX());
        upperArc.setCenterY(canvasPosition.getY());

        upperArc.setLength(180);
        upperArc.setRadiusX(30);
        upperArc.setRadiusY(20);
        upperArc.setStartAngle(0);
        upperArc.setType(ArcType.OPEN);

        // lowerArc definition
        Arc lowerArc = new Arc();
        lowerArc.setCenterX(canvasPosition.getX());
        lowerArc.setCenterY(canvasPosition.getY());
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
            upperArc.setStroke(device.isInWireEnergized() ? Color.YELLOW : Color.BLACK);
            lowerArc.setStroke(device.isOutWireEnergized() ? Color.YELLOW: Color.BLACK);

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
