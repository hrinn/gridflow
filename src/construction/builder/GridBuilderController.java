package construction.builder;

import application.events.*;
import construction.*;
import domain.Grid;
import domain.geometry.Point;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.UUID;

public class GridBuilderController implements PropertiesObserver {

    private GridBuilder model;
    private GridFlowEventManager gridFlowEventManager;
    private WireExtendContext wireExtendContext;
    private BuildMenuData buildData;
    private PropertiesData propertiesData;

    public GridBuilderController(Grid grid, GridFlowEventManager gridFlowEventManager,
                                 WireExtendContext wireExtendContext, BuildMenuData buildMenuData,
                                 PropertiesData propertiesData) {
        this.model = new GridBuilder(grid, propertiesData);
        this.gridFlowEventManager = gridFlowEventManager;
        this.wireExtendContext = wireExtendContext;
        this.buildData = buildMenuData;
        this.propertiesData = new PropertiesData();
        PropertiesManager.attach(this);
    }

    public void buildDataChanged() {
        wireExtendContext.placing = false;
    }

    @Override
    public void updateProperties(PropertiesData PD) {
        this.propertiesData = new PropertiesData(PD.getType(), PD.getID(), PD.getName(),
                PD.getDefaultState(), PD.getRotation());
    }

    public void propertiesDataChanged() {

    }

    private final EventHandler<MouseEvent> placeWireEventHandler = event -> {
        if (buildData.toolType != ToolType.WIRE) return;
        if (!event.isPrimaryButtonDown()) return;

        // for implementing connecting/extending, try and reuse existing code
        if (wireExtendContext.placing) { // end placement
            wireExtendContext.placing = false;
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
            wireExtendContext.placing = true;
            wireExtendContext.beginPoint = Point.nearestCoordinate(event.getX(), event.getY());
        }
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

        // Change the UUID back to default for placing of next component
        this.propertiesData.setID(new UUID(0, 0));
        PropertiesManager.notifyObservers(this.propertiesData);

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
