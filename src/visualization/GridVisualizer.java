package visualization;

import application.canvas.PannableCanvas;
import javafx.scene.Group;
import domain.Grid;
import domain.components.*;
import visualization.componentIcons.*;

public class GridVisualizer {

    public static final double UNIT = 20;
    public static final double STROKE_WIDTH = 1.5;
    public static final double ENERGY_STROKE_WIDTH = 4;

    private final Grid grid;
    private final PannableCanvas canvas;

    public GridVisualizer(Grid grid, PannableCanvas canvas) {
        // add node gestures when canvas is created; figure this out
        //this.nodeGestures = new NodeGestures(canvas, eventManager, grid);

        this.grid = grid;
        this.canvas = canvas;
    }

    private void addComponentIconToCanvas(ComponentIcon icon) {
        Group componentNode = icon.getComponentNode();
        Group energyOutlineNodes = icon.getEnergyOutlineNodes();
        //componentNode.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());

        canvas.getChildren().addAll(energyOutlineNodes, componentNode);

        componentNode.toFront();
        energyOutlineNodes.toBack();
    }

    public void displayGrid() {
        clearGrid();

        for (Component component : grid.getComponents()) {
            addComponentIconToCanvas(component.getComponentIcon());
        }
    }

    public void clearGrid() {
        canvas.getChildren().clear();
    }

}
