package construction.selector;

import construction.PropertiesData;
import construction.canvas.GridCanvasFacade;
import domain.Association;
import domain.Grid;
import domain.Selectable;
import domain.components.Breaker;
import domain.components.Closeable;
import domain.components.Component;
import domain.components.Wire;
import domain.geometry.Point;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SelectionManager {

    private List<String> selectedIDs;
    private ObservableList<String> observedSelectedIDs;
    private Rectangle selectionBox;
    private GridCanvasFacade canvasFacade;
    private Grid grid;
    private Point mouseDownPoint;

    public SelectionManager(GridCanvasFacade canvasFacade, Grid grid) {
        selectedIDs = new ArrayList<>();
        observedSelectedIDs = FXCollections.observableList(selectedIDs);
        selectionBox = new Rectangle();
        this.canvasFacade = canvasFacade;
        this.grid = grid;

        selectionBox.setFill(Color.TRANSPARENT);
        selectionBox.setStroke(Color.BLACK);
        selectionBox.getStrokeDashArray().add(10.0);

        // set up action on selectIDs list changed
//        observedSelectedIDs.addListener((ListChangeListener<String>) change -> {
//            int numSelected = selectedIDs.size();
//            PropertiesData properties = new PropertiesData();
//
//            if (numSelected == 1) {
//                properties.setNumSelected(numSelected);
//                Selectable sel = grid.getSelectableByID(selectedIDs.get(0));
//
//                if (sel != null) {
//                    properties.setID(UUID.fromString(selectedIDs.get(0)));
//
//                    if (sel instanceof Component) {
//                        properties.setType(((Component)sel).getComponentType());
//                        properties.setName(((Component)sel).getName());
//                        properties.setRotation(((Component)sel).getAngle());
//
//                        // if component can have its state changed, update default state, else leave alone
//                        if (sel instanceof Closeable) {
//                            properties.setDefaultState(((Closeable) sel).isClosedByDefault());
//                        }
//
//                        // if comp name layout bounds match, update properties on reselection
//                        if (((Component)sel).getComponentIcon().getActiveLeft()) {
//                            properties.setNamePos(true);
//                        }
//
//                    } else if (sel instanceof Association) {
//                        properties.setAssociation(true);
//                        properties.setAssocLabel(((Association)sel).getLabel());
//                        properties.setAssocSubLabel(((Association)sel).getSubLabel());
//                        properties.setAssocAcronym(((Association)sel).getAcronym());
//                    }
//                }
//
//            } else if (numSelected == 2) {
//                // Used for tandemable components (2 breakers)
//                properties.setDefaultComponentProperties(numSelected);
//                Selectable selTandem1 = grid.getSelectableByID(selectedIDs.get(0));
//                Selectable selTandem2 = grid.getSelectableByID(selectedIDs.get(1));
//                if (selTandem1 instanceof Breaker && selTandem2 instanceof Breaker &&
//                        (((Breaker) selTandem1).getComponentType() == ((Breaker) selTandem2).getComponentType())) {
//                    // Selected components are both breakers of the same type, add them to the tandem list and present options
//                    properties.addTandemComp((Breaker) selTandem1);
//                    properties.addTandemComp((Breaker) selTandem2);
//                }
//
//            } else {
//                properties.setDefaultComponentProperties(numSelected);
//            }
//
//            // update the properties observers
//            PropertiesManager.notifyObservers(properties);
//        });
    }

    void selectAll() {
        deSelectAll();
        grid.getComponents().forEach(comp -> {
            comp.setSelect(true);
            selectedIDs.add(comp.getId().toString());
        });

        grid.getAssociations().forEach(assoc -> {
            assoc.setSelect(true);
            selectedIDs.add(assoc.getID().toString());
        });
    }

    // deletes all items in the selected ids list
    // returns the number of items deleted
    public int deleteSelectedItems() {
        // Wires cannot be deleted if they are connected to a device or source, so delete the selected devices first
        sortWiresToBack();
        observedSelectedIDs.forEach(id -> {
            setSelect(id, false);
            grid.deleteSelectedItem(id);
        });
        int nitems = observedSelectedIDs.size();
        observedSelectedIDs.clear();
        return nitems;
    }

    private void setSelect(String ID, boolean select) {
        Selectable item = grid.getSelectableByID(ID);
        item.setSelect(select);
    }

    public void deSelectAll() {
        observedSelectedIDs.forEach(ID -> setSelect(ID, false));
        observedSelectedIDs.clear();
    }

    public void continuousPointSelection(String ID) {
        if (observedSelectedIDs.contains(ID)) return;
        setSelect(ID, true);
        observedSelectedIDs.add(ID);
    }

    public void pointSelection(String ID) {
        deSelectAll();
        setSelect(ID, true);
        observedSelectedIDs.add(ID);
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
        List<String> intersectingIDs = new ArrayList<>();
        intersectingIDs.addAll(getIntersectingBoundingRectIDs());
        intersectingIDs.addAll(getIntersectingAssociationIDs());

        intersectingIDs.forEach(id -> {
            if (!observedSelectedIDs.contains(id)) {
                setSelect(id, true);
                observedSelectedIDs.add(id);
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

    private List<String> getIntersectingBoundingRectIDs() {
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

    private List<String> getIntersectingAssociationIDs() {
        List<String> IDList = new ArrayList<>();
        List<Node> selectableNodes = new ArrayList<>();
        // gets all the selectable nodes of every association
        grid.getAssociations().forEach(association ->
                selectableNodes.addAll(association.getAssociationIcon().getSelectableNodes()));

        for (Node node : selectableNodes) {
            if (selectionBox.getBoundsInParent().intersects(node.getBoundsInParent())) {
                IDList.add(node.getId());
            }
        }

        return IDList;
    }

    private void sortWiresToBack() {
        observedSelectedIDs.sort((ID1, ID2) -> {
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
