package construction.selector;

import construction.canvas.GridCanvasFacade;
import domain.Grid;
import domain.components.Wire;
import domain.geometry.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SelectionManager {

    private List<String> selectedComponentIDs;
    private Rectangle selectionBox;
    private GridCanvasFacade canvasFacade;
    private Grid grid;
    private Point mouseDownPoint;

    public SelectionManager(GridCanvasFacade canvasFacade, Grid grid) {
        selectedComponentIDs = new ArrayList<>();
        selectionBox = new Rectangle();
        this.canvasFacade = canvasFacade;
        this.grid = grid;

        selectionBox.setFill(Color.TRANSPARENT);
        selectionBox.setStroke(Color.BLACK);
        selectionBox.getStrokeDashArray().add(10.0);
    }


    // list of component icons is bad, causing issues

    public void deleteSelectedComponents() {
        // Wires cannot be deleted if they are connected to a device or source, so delete the selected devices first
        sortWiresToBack();
        for (String ID : selectedComponentIDs) {
            grid.deleteComponent(ID);
        }
        selectedComponentIDs.clear();
    }

    public void deSelectAll() {
        selectedComponentIDs.clear();
        canvasFacade.deSelectAll();
    }

    public void continuousPointSelection(String ID) {
        if (selectedComponentIDs.contains(ID)) return;
        canvasFacade.selectComponent(ID);
        selectedComponentIDs.add(ID);
    }

    public void pointSelection(String ID) {
        deSelectAll();
        canvasFacade.selectComponent(ID);
        selectedComponentIDs.add(ID);
    }

    public void beginSelection(double x, double y) {
        mouseDownPoint = new Point(x, y);
        deSelectAll();

        selectionBox.setX(mouseDownPoint.getX());
        selectionBox.setX(mouseDownPoint.getY());
        selectionBox.setWidth(0);
        selectionBox.setHeight(0);
        canvasFacade.clearOverlay();
        canvasFacade.addOverlayNode(selectionBox);
    }

    public void expandSelection(double x, double y) {
        selectionBox.setX(Math.min(x, mouseDownPoint.getX()));
        selectionBox.setWidth(Math.abs(x - mouseDownPoint.getX()));
        selectionBox.setY(Math.min(y, mouseDownPoint.getY()));
        selectionBox.setHeight(Math.abs(y - mouseDownPoint.getY()));
    }

    public void endSelection() {
        // detect selection box overlap
        getSelectedNodeIDs().forEach(id -> {
            if (!selectedComponentIDs.contains(id)) {
                canvasFacade.selectComponent(id);
                selectedComponentIDs.add(id);
            }
        });
        canvasFacade.clearOverlay();
    }

    private List<String> getSelectedNodeIDs() {
        List<String> IDList = new ArrayList<>();
        List<Rectangle> existingBoundingRects = canvasFacade.getAllBoundingRects();

        for (Rectangle boundingRect : existingBoundingRects) {
            if (rectsOverlap(boundingRect, selectionBox)) {
                IDList.add(boundingRect.getId());
            }
        }

        return IDList;
    }

    private boolean rectsOverlap(Rectangle r1, Rectangle r2) {
        return r1.getX() < r2.getX() + r2.getWidth()
                && r1.getX() + r1.getWidth() > r2.getX()
                && r1.getY() < r2.getY() + r2.getHeight()
                && r1.getY() + r1.getHeight() > r2.getY();
    }



    private void sortWiresToBack() {
        selectedComponentIDs.sort((ID1, ID2) -> {
            if (grid.getComponent(ID1) instanceof Wire) {
                if (grid.getComponent(ID2) instanceof Wire) {
                    // comp1 and comp2 are wires
                    return 0;
                } else {
                    // comp1 is wire, comp2 is not wire
                    return 1;
                }
            } else {
                if (grid.getComponent(ID2) instanceof Wire) {
                    // comp1 is not wire, comp2 is wire
                    return -1;
                } else {
                    // comp1 and comp2 are not wires
                    return 0;
                }
            }
        });
    }
}
