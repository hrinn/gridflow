package visualization;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import main.events.Event;
import main.events.EventManager;
import main.events.IEventListener;
import model.Grid;
import model.components.*;
import visualization.componentIcons.*;

public class GridVisualizer implements IEventListener {

    private final PannableCanvas canvas = new PannableCanvas(12000, 6000);
    private final Grid grid;
    private final NodeGestures nodeGestures;

    public GridVisualizer(Grid grid, EventManager eventManager) {
        this.grid = grid;
        this.nodeGestures = new NodeGestures(canvas, eventManager, grid);

        eventManager.addListener(this);
        canvas.setTranslateX(-5350);
        canvas.setTranslateY(-2650);
        setSceneGestures();
    }

    private void setSceneGestures() {
        SceneGestures sceneGestures = new SceneGestures(canvas);
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        canvas.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
    }

    public void handleEvent(Event event) {
        if (event == Event.GridEnergized) {
            displayGrid();
        }
    }

    public PannableCanvas getCanvas() {
        return canvas;
    }

    private void addComponentIconToCanvas(ComponentIcon icon) {
        Group componentNode = icon.getComponentNode();
        Group energyOutlineNode = icon.getEnergyOutlineNodes();
        componentNode.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());

        canvas.getChildren().addAll(energyOutlineNode, componentNode);

        componentNode.toFront();
        energyOutlineNode.toBack();
    }

    public void displayGrid() {
        clearGrid();

        for (Component component : grid.getComponents()) {
            addComponentIconToCanvas(component.getComponentIcon());
        }
    }

    private void clearGrid() {
        canvas.getChildren().clear();
    }

}
