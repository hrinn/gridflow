package construction.builder;

import application.events.GridFlowEvent;
import application.events.GridFlowEventManager;
import construction.*;
import domain.Grid;
import domain.geometry.Point;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class GridBuilderController {

    private GridBuilder model;
    private GridFlowEventManager gridFlowEventManager;
    private WireExtendContext wireExtendContext;
    private BuildMenuData buildData;

    public GridBuilderController(Grid grid, PropertiesData properties, GridFlowEventManager gridFlowEventManager,
                                 WireExtendContext wireExtendContext) {
        this.model = new GridBuilder(grid, properties);
        this.gridFlowEventManager = gridFlowEventManager;
        this.wireExtendContext = wireExtendContext;
    }

    public void updateBuildMenuData(BuildMenuData buildData) {
        this.buildData = buildData;
    }

    private final EventHandler<MouseEvent> placeWireEventHandler = event -> {
        if (buildData.toolType != ToolType.WIRE) return;
        if (event.isSecondaryButtonDown()) return;

        // for implementing connecting/extending, try and reuse existing code
        if (wireExtendContext.placing) { // end placement
            wireExtendContext.placing = false;
            Point endPoint = Point.nearestCoordinate(event.getX(), event.getY());
            Point lockedEndPoint = endPoint.clampPerpendicular(wireExtendContext.beginPoint);
            model.placeWire(wireExtendContext.beginPoint, lockedEndPoint);
            gridFlowEventManager.sendEvent(GridFlowEvent.WirePlaced);
            gridFlowEventManager.sendEvent(GridFlowEvent.GridChanged);

        } else { // begin placement
            wireExtendContext.placing = true;
            wireExtendContext.beginPoint = Point.nearestCoordinate(event.getX(), event.getY());
        }
    };

    private final EventHandler<MouseEvent> toggleComponentEventHandler = event -> {
        if (buildData.toolType != ToolType.INTERACT) return;
        if (event.isSecondaryButtonDown()) return;

        String targetId = ((Node)event.getTarget()).getId();
        model.toggleComponent(targetId);
        gridFlowEventManager.sendEvent(GridFlowEvent.GridChanged);

        event.consume();

    };

    private final EventHandler<MouseEvent> placeComponentEventHandler = event -> {
        if (buildData.toolType != ToolType.PLACE) return;
        if (event.isSecondaryButtonDown()) return;

        Point coordPoint = Point.nearestCoordinate(event.getX(), event.getY());
        model.placeComponent(coordPoint, buildData.componentType);
        gridFlowEventManager.sendEvent(GridFlowEvent.GridChanged); // should only send this event if place comp returns true

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
}
