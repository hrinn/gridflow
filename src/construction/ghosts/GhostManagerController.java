package construction.ghosts;

import application.events.Event;
import application.events.IEventListener;
import construction.*;
import construction.canvas.GridCanvasFacade;
import domain.geometry.Point;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class GhostManagerController implements IEventListener {

    private GhostManager model;
    private GridCanvasFacade canvasFacade;
    private WireExtendContext wireExtendContext;
    private BuildMenuData buildData;

    private static final Image errorCursorImage = new Image("/resources/error_cursor.png");
    public static final Cursor ERROR_CURSOR = new ImageCursor(errorCursorImage, errorCursorImage.getWidth()/2,
            errorCursorImage.getHeight()/2);

    public GhostManagerController(GridCanvasFacade canvasFacade, PropertiesData properties,
                                  WireExtendContext wireExtendContext) {
        this.model = new GhostManager(canvasFacade, properties);
        this.wireExtendContext = wireExtendContext;
        this.canvasFacade = canvasFacade;
    }

    public void handleEvent(Event event) {
        if (event == Event.WirePlaced) {
            model.setGhostIcon(ComponentType.WIRE);
        }
    }

    public void updateBuildMenuData(BuildMenuData buildData) {
        this.buildData = buildData;
        if (buildData.toolType == ToolType.PLACE || buildData.toolType == ToolType.WIRE) {
            model.enableGhostIcon();
            model.setGhostIcon(buildData.componentType);
        } else {
            model.disableGhostIcon();
        }
    }

    private final EventHandler<MouseEvent> enterComponentHoverEventHandler = event -> {
        if (!model.isGhostEnabled()) return;
        model.hideGhostIcon();
        canvasFacade.setCanvasCursor(ERROR_CURSOR);
        event.consume();
    };

    private final EventHandler<MouseEvent> exitComponentHoverEventHandler = event -> {
        if (!model.isGhostEnabled()) return;
        model.revealGhostIcon();
        canvasFacade.setCanvasCursor(Cursor.DEFAULT);
        event.consume();
    };

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

    public EventHandler<MouseEvent> getEnterComponentHoverEventHandler() {
        return enterComponentHoverEventHandler;
    }

    public EventHandler<MouseEvent> getExitComponentHoverEventHandler() {
        return exitComponentHoverEventHandler;
    }

    public EventHandler<MouseEvent> getGhostMoveEventHandler() {
        return ghostMoveEventHandler;
    }


}
