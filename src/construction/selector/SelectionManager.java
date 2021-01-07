package construction.selector;

import construction.canvas.GridCanvasFacade;
import domain.Grid;
import domain.components.Component;
import domain.components.Wire;
import domain.geometry.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        deSelectAll();
    }

    private void setSelect(String ID, boolean select) {
        Component comp = grid.getComponent(ID);
        if (comp != null) comp.getUpdatedComponentIcon().setSelect(select);
    }

    public void deSelectAll() {
        selectedComponentIDs.forEach(ID -> setSelect(ID, false));
        selectedComponentIDs.clear();
    }

    public void continuousPointSelection(String ID) {
        if (selectedComponentIDs.contains(ID)) return;
        setSelect(ID, true);
        selectedComponentIDs.add(ID);
    }

    public void pointSelection(String ID) {
        deSelectAll();
        setSelect(ID, true);
        selectedComponentIDs.add(ID);
    }

    public void beginSelection(double x, double y) {
        mouseDownPoint = new Point(x, y);
        deSelectAll();

        selectionBox.setX(mouseDownPoint.getX());
        selectionBox.setY(mouseDownPoint.getY());

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
                setSelect(id, true);
                selectedComponentIDs.add(id);
            }
        });
        clearSelection();
    }

    private void clearSelection() {
        canvasFacade.clearOverlay();
        selectionBox.setX(0);
        selectionBox.setY(0);
        selectionBox.setHeight(0);
        selectionBox.setWidth(0);
    }

    private void printSelectionBox() {
        System.out.println("Width: " + selectionBox.getWidth() + " Height: " + selectionBox.getHeight());
    }

    private List<String> getSelectedNodeIDs() {
        List<String> IDList = new ArrayList<>();
        List<Rectangle> existingBoundingRects = grid.getComponents().stream().map(comp ->
            comp.getUpdatedComponentIcon().getBoundingRect()
        ).collect(Collectors.toList());

        for (Rectangle boundingRect : existingBoundingRects) {
            if (selectionBox.getBoundsInParent().intersects(boundingRect.getBoundsInParent())) {
                IDList.add(boundingRect.getId());
            }
        }

        return IDList;
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
