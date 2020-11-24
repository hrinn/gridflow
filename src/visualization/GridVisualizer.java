package visualization;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import main.events.Event;
import main.events.EventManager;
import main.events.IEventListener;
import model.Grid;
import model.components.*;
import model.geometry.Point;
import visualization.components.ComponentIconCreator;
import visualization.components.DeviceIcon;
import visualization.components.SourceIcon;
import visualization.components.WireIcon;

public class GridVisualizer implements IEventListener {

    private PannableCanvas canvas = new PannableCanvas(12000, 6000);
    private Grid grid;
    private EventManager eventManager;
    private NodeGestures nodeGestures;

    public GridVisualizer(Grid grid, EventManager eventManager) {
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

        SourceIcon powerSourceIcon = ComponentIconCreator.getPowerSourceIcon(center.translate(0, 80));
        powerSourceIcon.setSourceNodeEnergyState(false);
        powerSourceIcon.setWireEnergyState(true, 0);
        addNodeToCanvas(powerSourceIcon);

        SourceIcon turbineIcon = ComponentIconCreator.getTurbineIcon(center.translate(60, 80));
        turbineIcon.setSourceNodeEnergyState(true);
        turbineIcon.setWireEnergyState(true, 0);
        turbineIcon.setWireEnergyState(true, 1);
        addNodeToCanvas(turbineIcon);

        WireIcon wireIcon1 = ComponentIconCreator.getWireIcon(center.translate(100, 90), center.translate(180, 90));
        wireIcon1.setWireIconEnergyState(true);
        addNodeToCanvas(wireIcon1);

        WireIcon wireIcon2 = ComponentIconCreator.getWireIcon(center.translate(100, 110), center.translate(180, 110));
        wireIcon2.setWireIconEnergyState(false);
        addNodeToCanvas(wireIcon2);
    }

    public void handleEvent(Event event) {
        if (event == Event.GridEnergized) {
            displayGrid();
        }
    }

    public PannableCanvas getGridCanvas() {
        return canvas;
    }

    private void addNodeToCanvas(Node node) {
        canvas.getChildren().add(node);
        // sort children so that energy outlines are behind all other shapes
        node.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
    }



    public void displayGrid() {
        clearGraph();

        for (Component component : grid.getComponents()) {
            addComponent(component);
        }
    }

    private void addComponent(Component component) {
        addNodeToCanvas(component.getComponentIcon());
    }

    private void clearGraph() {
        canvas.getChildren().clear();
    }

}
