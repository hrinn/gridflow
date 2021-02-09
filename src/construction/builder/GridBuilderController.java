package construction.builder;

import application.events.*;
import construction.*;
import domain.Association;
import domain.Grid;
import domain.geometry.Point;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.w3c.dom.css.Rect;

// this controller handles event logic for grid building
// this is mostly user click while
public class GridBuilderController {

    private GridBuilder model;
    private GridFlowEventManager gridFlowEventManager;
    private BuildMenuData buildData;
    private PropertiesData propertiesData;
    private Grid grid;

    // these Contexts store data needed for actions that take place over time
    // used change borders of an association
    private AssociationMoveContext associationMoveContext = new AssociationMoveContext();
    // used to move the text of an association
    private DragContext dragContext = new DragContext();
    // used for double click actions, like placing a wire or placing an association
    private DoubleClickPlacementContext doubleClickPlacementContext;

    public GridBuilderController(Grid grid, GridFlowEventManager gridFlowEventManager,
                                 DoubleClickPlacementContext doubleClickPlacementContext, BuildMenuData buildMenuData,
                                 PropertiesData propertiesData) {
        this.model = new GridBuilder(grid, propertiesData);
        this.gridFlowEventManager = gridFlowEventManager;
        this.doubleClickPlacementContext = doubleClickPlacementContext;
        this.buildData = buildMenuData;
        this.propertiesData = propertiesData;
        this.grid = grid;
    }

    public void buildDataChanged() {
        doubleClickPlacementContext.placing = false;
    }

    public void propertiesDataChanged() {
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
            boolean res = model.placeWire(doubleClickPlacementContext.beginPoint, lockedEndPoint, ctrlPressed);
            if (res) {
                gridFlowEventManager.sendEvent(new GridChangedEvent());
            } else {
                gridFlowEventManager.sendEvent(new PlacementFailedEvent());
            }
            gridFlowEventManager.sendEvent(new WirePlacedEvent());

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
            model.placeAssociation(doubleClickPlacementContext.beginPoint, endPoint);
            gridFlowEventManager.sendEvent(new AssociationPlacedEvent());
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
        gridFlowEventManager.sendEvent(new GridChangedEvent());

        event.consume();

    };

    private final EventHandler<MouseEvent> placeComponentEventHandler = event -> {
        if (buildData.toolType != ToolType.PLACE) return;
        if (!event.isPrimaryButtonDown()) return;

        Point coordPoint = Point.nearestCoordinate(event.getX(), event.getY());

        boolean res = model.placeComponent(coordPoint, buildData.componentType);
        if (res) {
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
        associationMoveContext.targetPosition = Point.nearestCoordinate(event.getSceneX(), event.getSceneY());

        event.consume();
    };

    private final EventHandler<MouseEvent> resizeAssociationNWHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;
        if (!event.isPrimaryButtonDown()) return;
        if (associationMoveContext.targetPosition == null) return;

        Rectangle rect = associationMoveContext.target.getAssociationIcon().getRect();
        double handleRadius = 5;
        Point newLoc = Point.nearestCoordinate(event.getSceneX(), event.getSceneY());

        double deltaX = newLoc.getX() - associationMoveContext.targetPosition.getX();
        double deltaY = newLoc.getY() - associationMoveContext.targetPosition.getY();
        double newX = rect.getX() + deltaX ;
        if (newX >= handleRadius
                && newX <= rect.getX() + rect.getWidth() - handleRadius) {
            rect.setX(newX);
            rect.setWidth(rect.getWidth() - deltaX);
        }
        double newY = rect.getY() + deltaY ;
        if (newY >= handleRadius
                && newY <= rect.getY() + rect.getHeight() - handleRadius) {
            rect.setY(newY);
            rect.setHeight(rect.getHeight() - deltaY);
        }
        associationMoveContext.targetPosition = Point.nearestCoordinate(event.getSceneX(), event.getSceneY());
        event.consume();
    };

    private final EventHandler<MouseEvent> resizeAssociationSEHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;
        if (!event.isPrimaryButtonDown()) return;
        if (associationMoveContext.targetPosition == null) return;


        Point newLoc = Point.nearestCoordinate(event.getSceneX(), event.getSceneY());

        Rectangle rect = associationMoveContext.target.getAssociationIcon().getRect();
        double handleRadius = 5;

        double deltaX = newLoc.getX() - associationMoveContext.targetPosition.getX();
        double deltaY = newLoc.getY() - associationMoveContext.targetPosition.getY();
        double newMaxX = rect.getX() + rect.getWidth() + deltaX ;
        if (newMaxX >= rect.getX()
                && newMaxX <= rect.getParent().getBoundsInLocal().getWidth() - handleRadius) {
            rect.setWidth(rect.getWidth() + deltaX);
        }
        double newMaxY = rect.getY() + rect.getHeight() + deltaY ;
        if (newMaxY >= rect.getY()
                && newMaxY <= rect.getParent().getBoundsInLocal().getHeight() - handleRadius) {
            rect.setHeight(rect.getHeight() + deltaY);
        }
        associationMoveContext.targetPosition = Point.nearestCoordinate(event.getSceneX(), event.getSceneY());
        event.consume();

    };

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

    public EventHandler<MouseEvent> getPlaceWireEventHandler() {
        return placeWireEventHandler;
    }

    public EventHandler<MouseEvent> getToggleComponentEventHandler() {
        return toggleComponentEventHandler;
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
}
