package construction.canvas;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.WireIcon;

import java.util.UUID;

public class GridCanvasFacade {

    // this is the master's baby. it adds nodes to and removes nodes from the canvas.
    private GridCanvas canvas;

    // Component Event Handlers
    private EventHandler<MouseEvent> toggleComponentEventHandler;
    private EventHandler<MouseEvent> selectSingleComponentHandler;

    // Association Event Handlers
    private EventHandler<MouseEvent> beginMoveAssociationBorderHandler;
    private EventHandler<MouseEvent> moveAssociationBorderHandler;
    private EventHandler<MouseEvent> endMoveAssociationBorderHandler;
    private EventHandler<MouseEvent> beginHoverAssociationBorderHandler;
    private EventHandler<MouseEvent> endHoverAssociationBorderHandler;

    public GridCanvasFacade() {
        createCanvas();
    }

    private void createCanvas() {
        canvas = new GridCanvas();
        canvas.setTranslateX(-5350); // get this from application settings?
        canvas.setTranslateY(-2650);

        // canvas events
        SceneGestures sceneGestures = new SceneGestures(canvas);

        // panning and scrolling
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getBeginPanEventHandler());
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnPanEventHandler());
        canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, sceneGestures.getEndPanEventHandler());
        canvas.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
    }

    public void addComponentIcon(ComponentIcon icon) {
        Group componentNode = icon.getComponentNode();
        Group energyOutlineNodes = icon.getEnergyOutlineNodes();
        Rectangle boundingRect = icon.getBoundingRect();
        boundingRect.addEventHandler(MouseEvent.MOUSE_PRESSED, toggleComponentEventHandler);
        boundingRect.addEventHandler(MouseEvent.MOUSE_PRESSED, selectSingleComponentHandler);

        canvas.componentGroup.getChildren().add(componentNode);
        canvas.energyOutlineGroup.getChildren().add(energyOutlineNodes);
        canvas.boundingRectGroup.getChildren().add(boundingRect);
        canvas.boundingRectGroup.getChildren().add(icon.getFittingRect());
    }

    public void addOverlayNode(Node overlayNode) {
        canvas.overlayGroup.getChildren().add(overlayNode);
    }

    public void clearOverlay() {
        canvas.overlayGroup.getChildren().clear();
    }

    public void clearComponentGroups() {
        canvas.componentGroup.getChildren().clear();
        canvas.energyOutlineGroup.getChildren().clear();
        canvas.boundingRectGroup.getChildren().clear();
    }

    public void clearAssociationGroup() {
        canvas.associationGroup.getChildren().clear();
    }

    public void addAssociationNode(Group association, UUID id) {
        canvas.associationGroup.getChildren().add(association);
        association.setId(id.toString());
        association.getChildren().stream().filter(child -> child instanceof Line).forEach(line -> {
            line.addEventHandler(MouseEvent.MOUSE_ENTERED, beginHoverAssociationBorderHandler);
            line.addEventHandler(MouseEvent.MOUSE_EXITED, endHoverAssociationBorderHandler);
            line.addEventHandler(MouseEvent.MOUSE_PRESSED, beginMoveAssociationBorderHandler);
        });
    }

    public void showBackgroundGrid(boolean show) {
        if (show) {
            canvas.backgroundGrid.setOpacity(1);
        } else {
            canvas.backgroundGrid.setOpacity(0);
        }
    }

    public void addCanvasEventHandler(EventType eventType, EventHandler eventHandler) {
        canvas.addEventHandler(eventType, eventHandler);
    }

    public void addCanvasEventFilter(EventType eventType, EventHandler eventHandler) {
        canvas.addEventFilter(eventType, eventHandler);
    }

    public GridCanvas getCanvas() {
        return canvas;
    }

    public void setToggleComponentEventHandler(EventHandler<MouseEvent> eventHandler) {
        this.toggleComponentEventHandler = eventHandler;
    }

    public void setSelectSingleComponentHandler(EventHandler<MouseEvent> selectSingleComponentHandler) {
        this.selectSingleComponentHandler = selectSingleComponentHandler;
    }

    public void setBeginHoverAssociationBorderHandler(EventHandler<MouseEvent> beginHoverAssociationBorderHandler) {
        this.beginHoverAssociationBorderHandler = beginHoverAssociationBorderHandler;
    }

    public void setMoveAssociationBorderHandler(EventHandler<MouseEvent> moveAssociationBorderHandler) {
        this.moveAssociationBorderHandler = moveAssociationBorderHandler;
    }

    public void setBeginMoveAssociationBorderHandler(EventHandler<MouseEvent> beginMoveAssociationBorderHandler) {
        this.beginMoveAssociationBorderHandler = beginMoveAssociationBorderHandler;
    }

    public void setEndHoverAssociationBorderHandler(EventHandler<MouseEvent> endHoverAssociationBorderHandler) {
        this.endHoverAssociationBorderHandler = endHoverAssociationBorderHandler;
    }

    public void setEndMoveAssociationBorderHandler(EventHandler<MouseEvent> endMoveAssociationBorderHandler) {
        this.endMoveAssociationBorderHandler = endMoveAssociationBorderHandler;
    }
}
