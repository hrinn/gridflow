package construction.ghosts;

import application.events.PlacementFailedEvent;
import application.events.GridFlowEvent;
import application.events.GridFlowEventListener;
import application.events.WirePlacedEvent;
import construction.*;
import construction.canvas.GridCanvasFacade;
import domain.geometry.Point;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class GhostManagerController implements GridFlowEventListener {

    private GhostManager model;
    private GridCanvasFacade canvasFacade;
    private DoubleClickPlacementContext wireExtendContext;
    private BuildMenuData buildData;

    public GhostManagerController(GridCanvasFacade canvasFacade, DoubleClickPlacementContext wireExtendContext,
                                  BuildMenuData buildMenuData, PropertiesData propertiesData) {
        this.model = new GhostManager(canvasFacade, propertiesData);
        this.wireExtendContext = wireExtendContext;
        this.canvasFacade = canvasFacade;
        this.buildData = buildMenuData;
    }

    public void handleEvent(GridFlowEvent gridFlowEvent) {
        if (gridFlowEvent instanceof WirePlacedEvent) {
            model.setGhostIcon(ComponentType.WIRE);
        } else if (gridFlowEvent instanceof PlacementFailedEvent) {
            handlePlacementError();
        }
    }

    private void handlePlacementError() {
        System.err.println("Placement Error");
        model.showGhostError();
    }

    public void buildMenuDataChanged() {
        if (buildData.toolType == ToolType.PLACE || buildData.toolType == ToolType.WIRE) {
            model.enableGhostIcon();
            model.setGhostIcon(buildData.componentType);
        } else {
            model.disableGhostIcon();
        }
    }

    public void propertiesDataChanged(boolean rotationChanged, boolean defaultStateChanged) {
        if (buildData.toolType == ToolType.PLACE) {
            if (rotationChanged) model.rotateGhostIcon();
            if (defaultStateChanged) model.updateGhostIcon(buildData.componentType);
        }
    }

    private final EventHandler<MouseEvent> ghostMoveEventHandler = event -> {
        if (!model.isGhostEnabled()) return;
        Point coordPoint = Point.nearestCoordinate(event.getX(), event.getY());

        if (wireExtendContext.placing) {
            Point endPoint = coordPoint.clampPerpendicular(wireExtendContext.beginPoint);
            model.extendGhostWire(wireExtendContext.beginPoint, endPoint);
        } else {
            model.updateGhostPosition(coordPoint);
        }
    };

    public EventHandler<MouseEvent> getGhostMoveEventHandler() {
        return ghostMoveEventHandler;
    }


}
