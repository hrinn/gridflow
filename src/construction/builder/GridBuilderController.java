package construction.builder;

import application.events.*;
import construction.*;
import construction.buildMenu.BuildMenuData;
import construction.canvas.GridCanvasFacade;
import construction.history.GridMemento;
import construction.properties.objectData.ObjectData;
import construction.properties.PropertiesData;
import construction.properties.PropertiesMenuFunctions;
import domain.Association;
import domain.Grid;
import domain.components.Breaker;
import domain.components.Component;
import domain.geometry.Point;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.List;
import java.util.UUID;
// this controller handles event logic for grid building
// this is mostly user click while

public class GridBuilderController {

    private GridBuilder model;
    private GridFlowEventManager gridFlowEventManager;
    private BuildMenuData buildData;
    private PropertiesData propertiesData;
    private Grid grid;
    private GridCanvasFacade canvasFacade;

    // these Contexts store data needed for actions that take place over time
    // used change borders of an association
    private AssociationMoveContext associationMoveContext = new AssociationMoveContext();
    // used to move the text of an association
    private DragContext dragContext = new DragContext();
    // used for double click actions, like placing a wire or placing an association
    private DoubleClickPlacementContext doubleClickPlacementContext;

    public GridBuilderController(Grid grid, GridFlowEventManager gridFlowEventManager,
                                 DoubleClickPlacementContext doubleClickPlacementContext, BuildMenuData buildMenuData,
                                 PropertiesData propertiesData, GridCanvasFacade canvasFacade) {
        this.model = new GridBuilder(grid, propertiesData);
        this.gridFlowEventManager = gridFlowEventManager;
        this.doubleClickPlacementContext = doubleClickPlacementContext;
        this.buildData = buildMenuData;
        this.propertiesData = new PropertiesData();
        this.grid = grid;
        this.canvasFacade = canvasFacade;
    }

    public void buildDataChanged() {
        doubleClickPlacementContext.placing = false;
        updateAssociationHandles(buildData.toolType == ToolType.ASSOCIATION);
    }

    public void linkTandems(List<Breaker> tandems) {
        // Unlink them first always
        unlinkTandems(tandems);
        // Check if tandems closed
        if (tandems.get(0).isClosed()) { tandems.get(0).toggleState(); }
        if (tandems.get(1).isClosed()) { tandems.get(1).toggleState(); }
        // Link them
        tandems.get(0).setTandemID(tandems.get(1).getId().toString());
        tandems.get(1).setTandemID(tandems.get(0).getId().toString());

        gridFlowEventManager.sendEvent(new GridChangedEvent());
    }


    public void unlinkTandems (List<Breaker> tandems) {
        if (tandems.get(0).hasTandem()) {
            unlinkTandem(tandems.get(0));
        }
        if (tandems.get(1).hasTandem()) {
            unlinkTandem(tandems.get(1));
        }
    }

    public void unlinkTandemByID (String brID) {
        Component comp = grid.getComponent(brID);
        if (comp instanceof Breaker) {
            if (((Breaker) comp).hasTandem()){
                unlinkTandem(((Breaker) comp));
            }
        }
    }

    public void unlinkTandem (Breaker br) {
        Component comp = grid.getComponent(br.getTandemID());
        if (comp instanceof Breaker) {
            ((Breaker) comp).setTandemID("");
        }
        br.setTandemID("");
    }

    public boolean isBreaker(UUID ID) {
        Component comp = grid.getComponent(ID.toString());
        return comp instanceof Breaker;
    }


    // this event handler is for placing wires with the wire tool
    // it is run once per click, so the event either begin a placement or finishes a placement
    private final EventHandler<MouseEvent> placeWireEventHandler = event -> {
        if (buildData.toolType != ToolType.WIRE) return;
        if (!event.isPrimaryButtonDown()) return;

        if (doubleClickPlacementContext.placing) { // end placement
            doubleClickPlacementContext.placing = false;
            Point endPoint = Point.nearestCoordinate(event.getX(), event.getY());
            Point lockedEndPoint = endPoint.clampPerpendicular(doubleClickPlacementContext.beginPoint);
            boolean ctrlPressed = event.isControlDown();
            GridMemento prePlaceWireMemento = grid.makeSnapshot(); // create a snapshot of the grid before the wire is placed
            boolean res = model.placeWire(doubleClickPlacementContext.beginPoint, lockedEndPoint, ctrlPressed);
            if (res) {
                // if the wire was successfully placed
                gridFlowEventManager.sendEvent(new SaveStateEvent(prePlaceWireMemento)); // save the snapshot to the undo history
                GridChangedEvent e = new GridChangedEvent();
                e.toolCausingChange = ToolType.WIRE;
                gridFlowEventManager.sendEvent(e);
            } else {
                gridFlowEventManager.sendEvent(new PlacementFailedEvent());
            }

        } else { // begin placement
            doubleClickPlacementContext.placing = true;
            doubleClickPlacementContext.beginPoint = Point.nearestCoordinate(event.getX(), event.getY());
        }

        event.consume();
    };

    // this event handler if for placing associations with the association tool
    // it is run once per click, like placing wires
    private final EventHandler<MouseEvent> placeAssociationEventHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;
        if (!event.isPrimaryButtonDown()) return;

        if (doubleClickPlacementContext.placing) { // end placement
            doubleClickPlacementContext.placing = false;
            Point endPoint = Point.nearestCoordinate(event.getX(), event.getY());
            gridFlowEventManager.sendEvent(new SaveStateEvent(grid.makeSnapshot())); // save the state of the grid before change
            model.placeAssociation(doubleClickPlacementContext.beginPoint, endPoint);
            GridChangedEvent e = new GridChangedEvent();
            e.toolCausingChange = ToolType.ASSOCIATION;
            gridFlowEventManager.sendEvent(e);
        } else { // begin placement
            doubleClickPlacementContext.placing = true;
            doubleClickPlacementContext.beginPoint = Point.nearestCoordinate(event.getX(), event.getY());
        }

        event.consume();
    };

    private final EventHandler<MouseEvent> toggleComponentEventHandler = event -> {
        if (buildData.toolType != ToolType.INTERACT) return;
        if (!event.isPrimaryButtonDown()) return;

        String targetId = ((Node)event.getTarget()).getId();
        model.toggleComponent(targetId);
        gridFlowEventManager.sendEvent(new GridChangedEvent()); // toggling components does not create a snapshot to save processing power

        event.consume();

    };

    private final EventHandler<MouseEvent> lockComponentEventHandler = event -> {
        if (buildData.toolType != ToolType.INTERACT) return;
        if (!event.isSecondaryButtonDown()) return;

        String targetId = ((Node)event.getTarget()).getId();
        model.lockComponent(targetId);
        gridFlowEventManager.sendEvent(new GridChangedEvent());

        event.consume();
    };

    private final EventHandler<MouseEvent> placeComponentEventHandler = event -> {
        if (buildData.toolType != ToolType.PLACE) return;
        if (!event.isPrimaryButtonDown()) return;

        Point coordPoint = Point.nearestCoordinate(event.getX(), event.getY());

        SaveStateEvent e = new SaveStateEvent(grid.makeSnapshot()); // create a snapshot of the grid before placing component
        boolean res = model.placeComponent(coordPoint, buildData.componentType);
        if (res) {
            gridFlowEventManager.sendEvent(e); // save the pre place grid state
            gridFlowEventManager.sendEvent(new GridChangedEvent());
        } else {
            gridFlowEventManager.sendEvent(new PlacementFailedEvent());
        }

        event.consume();
    };

    private final EventHandler<MouseEvent> beginResizeAssociationEventHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;
        if (!event.isPrimaryButtonDown()) return;

        // determine which association was targeted
        String targetID = ((Node)event.getTarget()).getId();
        Association target = grid.getAssociation(targetID);
        if (target == null) {
            System.err.println("Resize Association Handler, ID is null");
            return;
        }
        associationMoveContext.target = target;
        // determine beginning point for move
        associationMoveContext.targetPosition = Point.nearestCoordinate(event.getX(), event.getY());

        event.consume();
    };

    private final EventHandler<MouseEvent> resizeAssociationNWHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;
        if (!event.isPrimaryButtonDown()) return;
        if (associationMoveContext.targetPosition == null) return;

        Rectangle rect = associationMoveContext.target.getAssociationIcon().getRect();
        Point newLoc = Point.nearestCoordinate(event.getX(), event.getY());

        double deltaX = newLoc.getX() - associationMoveContext.targetPosition.getX();
        double deltaY = newLoc.getY() - associationMoveContext.targetPosition.getY();
        double newX = rect.getX() + deltaX ;
        if (newX <= rect.getX() + rect.getWidth()) {
            rect.setX(newX);
            rect.setWidth(rect.getWidth() - deltaX);
        }
        double newY = rect.getY() + deltaY ;
        if (newY <= rect.getY() + rect.getHeight()) {
            rect.setY(newY);
            rect.setHeight(rect.getHeight() - deltaY);
        }
        associationMoveContext.targetPosition = newLoc;
        event.consume();
    };

    private final EventHandler<MouseEvent> resizeAssociationSEHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;
        if (!event.isPrimaryButtonDown()) return;
        if (associationMoveContext.targetPosition == null) return;


        Point position = Point.nearestCoordinate(event.getX(), event.getY());
        Rectangle rect = associationMoveContext.target.getAssociationIcon().getRect();

        double deltaX = position.getX() - associationMoveContext.targetPosition.getX();
        double deltaY = position.getY() - associationMoveContext.targetPosition.getY();
        double newMaxX = rect.getX() + rect.getWidth() + deltaX ;
        if (newMaxX >= rect.getWidth()) {
            rect.setWidth(rect.getWidth() + deltaX);
        }
        double newMaxY = rect.getY() + rect.getHeight() + deltaY ;
        if (newMaxY >= rect.getY() && newMaxY >= rect.getHeight()) {
            rect.setHeight(rect.getHeight() + deltaY);
        }
        associationMoveContext.targetPosition = position;

        event.consume();
    };

    private final EventHandler<MouseEvent> resizeAssociationNEHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;
        if (!event.isPrimaryButtonDown()) return;
        if (associationMoveContext.targetPosition == null) return;


        Point position = Point.nearestCoordinate(event.getX(), event.getY());
        Rectangle rect = associationMoveContext.target.getAssociationIcon().getRect();

        double deltaX = position.getX() - associationMoveContext.targetPosition.getX();
        double deltaY = position.getY() - associationMoveContext.targetPosition.getY();
        double newMaxX = rect.getX() + rect.getWidth() + deltaX ;
        if (newMaxX >= rect.getWidth()) {
            rect.setWidth(rect.getWidth() + deltaX);
        }
        double newY = rect.getY() + deltaY ;
        if (newY <= rect.getY() + rect.getHeight()) {
            rect.setY(newY);
            rect.setHeight(rect.getHeight() - deltaY);
        }
        associationMoveContext.targetPosition = position;

        event.consume();
    };

    private final EventHandler<MouseEvent> resizeAssociationSWHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;
        if (!event.isPrimaryButtonDown()) return;
        if (associationMoveContext.targetPosition == null) return;


        Point position = Point.nearestCoordinate(event.getX(), event.getY());
        Rectangle rect = associationMoveContext.target.getAssociationIcon().getRect();

        double deltaX = position.getX() - associationMoveContext.targetPosition.getX();
        double deltaY = position.getY() - associationMoveContext.targetPosition.getY();
        double newX = rect.getX() + deltaX ;
        if (newX <= rect.getX() + rect.getWidth()) {
            rect.setX(newX);
            rect.setWidth(rect.getWidth() - deltaX);
        }
        double newMaxY = rect.getY() + rect.getHeight() + deltaY;
        if (newMaxY >= rect.getY() && newMaxY >= rect.getHeight()) {
            rect.setHeight(rect.getHeight() + deltaY);
        }
        associationMoveContext.targetPosition = position;

        event.consume();
    };

    // hides or shows the association handles based on current selected tool
    private void updateAssociationHandles(boolean show) {
        grid.getAssociations().forEach(association -> association.getAssociationIcon().showHandles(show));
    }

    // runs when the use begins dragging an association's label
    private final EventHandler<MouseEvent> beginAssociationTextDragEventHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;
        if (!event.isPrimaryButtonDown()) return;

        Text target = (Text)event.getTarget();

        // original position
        dragContext.mouseAnchorX = event.getSceneX();
        dragContext.mouseAnchorY = event.getSceneY();
        dragContext.translateAnchorX = target.getTranslateX();
        dragContext.translateAnchorY = target.getTranslateY();

        event.consume();
    };

    // runs while the user is dragging an association's label
    private final EventHandler<MouseEvent> dragAssociationTextEventHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;
        if (!event.isPrimaryButtonDown()) return;

        // moves the text based on the mouse position
        // does so by translating the text and taking into account its original position
        double offsetX = event.getSceneX() - dragContext.mouseAnchorX;
        double offsetY = event.getSceneY() - dragContext.mouseAnchorY;
        double newTranslateX = dragContext.translateAnchorX + offsetX;
        double newTranslateY = dragContext.translateAnchorY + offsetY;

        Text target = (Text)event.getTarget();
        target.setTranslateX(newTranslateX);
        target.setTranslateY(newTranslateY);

        event.consume();
    };

    private final EventHandler<MouseEvent> showMoveCursorOnTextHoverHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;

        canvasFacade.getCanvas().setCursor(Cursor.MOVE);
        event.consume();
    };

    private final EventHandler<MouseEvent> showDefaultCursorOnLeaveTextHoverHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;

        canvasFacade.getCanvas().setCursor(Cursor.DEFAULT);
        event.consume();
    };

    public EventHandler<MouseEvent> getPlaceWireEventHandler() {
        return placeWireEventHandler;
    }

    public EventHandler<MouseEvent> getToggleComponentEventHandler() {
        return toggleComponentEventHandler;
    }

    public EventHandler<MouseEvent> getLockComponentEventHandler() {
        return lockComponentEventHandler;
    }

    public EventHandler<MouseEvent> getPlaceComponentEventHandler() {
        return placeComponentEventHandler;
    }

    public EventHandler<MouseEvent> getPlaceAssociationEventHandler() {
        return placeAssociationEventHandler;
    }

    public EventHandler<MouseEvent> getBeginResizeAssociationEventHandler() {
        return beginResizeAssociationEventHandler;
    }

    public EventHandler<MouseEvent> getBeginAssociationTextDragEventHandler() {
        return beginAssociationTextDragEventHandler;
    }

    public EventHandler<MouseEvent> getDragAssociationTextEventHandler() {
        return dragAssociationTextEventHandler;
    }

    public EventHandler<MouseEvent> getResizeAssociationNWHandler() {
        return resizeAssociationNWHandler;
    }

    public EventHandler<MouseEvent> getResizeAssociationSEHandler() {
        return resizeAssociationSEHandler;
    }

    public EventHandler<MouseEvent> getResizeAssociationNEHandler() {
        return resizeAssociationNEHandler;
    }

    public EventHandler<MouseEvent> getResizeAssociationSWHandler() {
        return resizeAssociationSWHandler;
    }

    public EventHandler<MouseEvent> getShowDefaultCursorOnLeaveTextHoverHandler() {
        return showDefaultCursorOnLeaveTextHoverHandler;
    }

    public EventHandler<MouseEvent> getShowMoveCursorOnTextHoverHandler() {
        return showMoveCursorOnTextHoverHandler;
    }
}
