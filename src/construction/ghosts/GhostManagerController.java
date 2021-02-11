package construction.ghosts;

import application.events.*;
import construction.*;
import construction.canvas.GridCanvasFacade;
import domain.geometry.Point;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

// This controller manages the events for all ghost related logic (the transparent icons that show when placing)
public class GhostManagerController implements GridFlowEventListener {

    private GhostManager ghostModel;
    private AssociationGhostManager associationModel;
    private DoubleClickPlacementContext doubleClickContext;
    private BuildMenuData buildData;

    public GhostManagerController(GridCanvasFacade canvasFacade, DoubleClickPlacementContext doubleClickContext,
                                  BuildMenuData buildMenuData, PropertiesData propertiesData) {
        this.ghostModel = new GhostManager(canvasFacade, propertiesData);
        this.associationModel = new AssociationGhostManager(canvasFacade);
        this.doubleClickContext = doubleClickContext;
        this.buildData = buildMenuData;
    }

    public void handleEvent(GridFlowEvent gridFlowEvent) {
        if (gridFlowEvent instanceof WirePlacedEvent) {
            ghostModel.setGhostIcon(ComponentType.WIRE);
        } else if (gridFlowEvent instanceof PlacementFailedEvent) {
            handlePlacementError();
        } else if (gridFlowEvent instanceof AssociationChangedEvent) {
            associationModel.setAssociationGhost();
        }
    }

    private void handlePlacementError() {
        System.err.println("Placement Error");
        ghostModel.showGhostError();
    }

    // switches the ghosts on or off when a different tool is selected
    public void buildMenuDataChanged() {
        if (buildData.toolType == ToolType.PLACE || buildData.toolType == ToolType.WIRE) {
            ghostModel.setGhostEnabled(true);
            ghostModel.setGhostIcon(buildData.componentType);
            associationModel.setGhostEnabled(false);
        } else if (buildData.toolType == ToolType.ASSOCIATION) {
            associationModel.setGhostEnabled(true);
            associationModel.setAssociationGhost();
            ghostModel.setGhostEnabled(false);
        } else {
            ghostModel.setGhostEnabled(false);
            associationModel.setGhostEnabled(false);
        }
    }

    public void propertiesDataChanged(boolean rotationChanged, boolean defaultStateChanged) {
        if (buildData.toolType == ToolType.PLACE) {
            if (rotationChanged) ghostModel.rotateGhostIcon();
            if (defaultStateChanged) ghostModel.updateGhostIcon(buildData.componentType);
        }
    }

    // responds to mouse moving and moves the ghost
    private final EventHandler<MouseEvent> ghostMoveEventHandler = event -> {
        if (ghostModel.isGhostEnabled()) {
            Point coordPoint = Point.nearestCoordinate(event.getX(), event.getY());

            if (doubleClickContext.placing) {
                Point endPoint = coordPoint.clampPerpendicular(doubleClickContext.beginPoint);
                ghostModel.extendGhostWire(doubleClickContext.beginPoint, endPoint);
            } else {
                ghostModel.updateGhostPosition(coordPoint);
            }
        } else if (associationModel.isGhostEnabled()) {
            Point coordPoint = Point.nearestCoordinate(event.getX(), event.getY());
            if (doubleClickContext.placing) {
                associationModel.setAssociationRectangleGhost(doubleClickContext.beginPoint, coordPoint);
            } else {
                associationModel.updateGhostPosition(coordPoint);
            }
        }
    };

    private final EventHandler<MouseEvent> endHoverAssociationHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;

        associationModel.setGhostEnabled(true);
        event.consume();
    };

    private final EventHandler<MouseEvent> beginHoverAssociationHandler = event -> {
        if (buildData.toolType != ToolType.ASSOCIATION) return;

        associationModel.setGhostEnabled(false);
        event.consume();
    };

    public EventHandler<MouseEvent> getGhostMoveEventHandler() {
        return ghostMoveEventHandler;
    }

    public EventHandler<MouseEvent> getEndHoverAssociationHandler() {
        return endHoverAssociationHandler;
    }

    public EventHandler<MouseEvent> getBeginHoverAssociationHandler() {
        return beginHoverAssociationHandler;
    }
}
