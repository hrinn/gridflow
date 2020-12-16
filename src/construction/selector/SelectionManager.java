package construction.selector;

import construction.canvas.GridCanvasFacade;
import domain.geometry.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class SelectionManager {

    private List<String> selectedComponentIDs;
    private Rectangle selectionBox;
    private GridCanvasFacade canvasFacade;
    private Point mouseDownPoint;

    public SelectionManager(GridCanvasFacade canvasFacade) {
        selectedComponentIDs = new ArrayList<>();
        selectionBox = new Rectangle();
        this.canvasFacade = canvasFacade;

        selectionBox.setFill(Color.TRANSPARENT);
        selectionBox.setStroke(Color.BLACK);
        //selectionBox.setStrokeDashOffset(40);
        selectionBox.getStrokeDashArray().add(10.0);
    }

    public void beginSelection(double x, double y) {
        mouseDownPoint = new Point(x, y);
        selectedComponentIDs.clear();
        canvasFacade.deSelectAll();

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
            canvasFacade.selectComponent(id);
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
}
