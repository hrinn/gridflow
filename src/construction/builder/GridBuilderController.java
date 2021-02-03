package construction.builder;

import application.events.*;
import construction.*;
import domain.Association;
import domain.Grid;
import domain.geometry.Point;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

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

    private final EventHandler<MouseEvent> beginMoveAssociationBorderEventHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;
        if (!event.isPrimaryButtonDown()) return;

        // determine which association was targeted
        Line target = ((Line)event.getTarget());

        associationMoveContext.target = grid.getAssociation(target.getId());
        // determine which line was targeted
        associationMoveContext.targetLine = getAssociationTargetLine(
                associationMoveContext.target.getTopleft(),
                new Point(target.getEndX(), target.getEndY()),
                associationMoveContext.target.getWidth(),
                associationMoveContext.target.getHeight()
        );
        associationMoveContext.moving = true;

        event.consume();
    };

    // takes the association's top left point, target line's end point, assoc's width, and assoc's height
    // and determines which line was targeted. (0 - top, 1 - right, 2 - bottom, 3 - left)
    private int getAssociationTargetLine(Point aTopLeft, Point tEnd, double w, double h) {
        if (aTopLeft.translate(w, 0).equals(tEnd)) {
            return 0;
        } else if (aTopLeft.translate(w, h).equals(tEnd)) {
            return 1;
        } else if (aTopLeft.translate(0, h).equals(tEnd)) {
            return 2;
        } else if (tEnd.equals(aTopLeft)) {
            return 3;
        } else {
            return -1;
        }
    }

    // runs when the use begins dragging an association's label
    private final EventHandler<MouseEvent> beginAssociationTextDragEventHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;
        if (!event.isPrimaryButtonDown()) return;

        Text target = (Text)event.getTarget();
        // original position
        dragContext.mouseAnchorX = target.getLayoutX();
        dragContext.mouseAnchorY = target.getLayoutY();

        dragContext.translateAnchorX = target.getTranslateX();
        dragContext.translateAnchorY = target.getTranslateY();

        event.consume();
    };

    // runs while the user is dragging an association's label
    private final EventHandler<MouseEvent> dragAssociationTextEventHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;
        if (!event.isPrimaryButtonDown()) return;

        Text target = (Text)event.getTarget();
        target.setTranslateX(event.getX() - dragContext.mouseAnchorX);
        target.setTranslateY(event.getY() - dragContext.mouseAnchorY);

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

    public EventHandler<MouseEvent> getBeginMoveAssociationBorderEventHandler() {
        return beginMoveAssociationBorderEventHandler;
    }

    public EventHandler<MouseEvent> getBeginAssociationTextDragEventHandler() {
        return beginAssociationTextDragEventHandler;
    }

    public EventHandler<MouseEvent> getDragAssociationTextEventHandler() {
        return dragAssociationTextEventHandler;
    }
}
