package construction.builder;

import application.events.*;
import construction.*;
import domain.Grid;
import domain.geometry.Point;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class GridBuilderController {

    private GridBuilder model;
    private GridFlowEventManager gridFlowEventManager;
    private DoubleClickPlacementContext doubleClickPlacementContext;
    private BuildMenuData buildData;
    private PropertiesData propertiesData;

    public GridBuilderController(Grid grid, GridFlowEventManager gridFlowEventManager,
                                 DoubleClickPlacementContext doubleClickPlacementContext, BuildMenuData buildMenuData,
                                 PropertiesData propertiesData) {
        this.model = new GridBuilder(grid, propertiesData);
        this.gridFlowEventManager = gridFlowEventManager;
        this.doubleClickPlacementContext = doubleClickPlacementContext;
        this.buildData = buildMenuData;
        this.propertiesData = propertiesData;
    }

    public void buildDataChanged() {
        doubleClickPlacementContext.placing = false;
    }

    public void propertiesDataChanged() {
    }

    private final EventHandler<MouseEvent> placeWireEventHandler = event -> {
        if (buildData.toolType != ToolType.WIRE) return;
        if (!event.isPrimaryButtonDown()) return;

        // for implementing connecting/extending, try and reuse existing code
        if (doubleClickPlacementContext.placing) { // end placement
            doubleClickPlacementContext.placing = false;
            Point endPoint = Point.nearestCoordinate(event.getX(), event.getY());
            Point lockedEndPoint = endPoint.clampPerpendicular(wireExtendContext.beginPoint);
            boolean ctrlPressed = event.isControlDown();
            boolean res = model.placeWire(wireExtendContext.beginPoint, lockedEndPoint, ctrlPressed);
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
}
