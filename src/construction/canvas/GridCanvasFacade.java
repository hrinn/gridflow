package construction.canvas;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import visualization.componentIcons.AssociationIcon;
import visualization.componentIcons.ComponentIcon;

public class GridCanvasFacade {

    // this is the master's baby. it adds nodes to and removes nodes from the canvas.
    private GridCanvas canvas;

    // Component Event Handlers
    private EventHandler<MouseEvent> toggleComponentEventHandler;
    private EventHandler<MouseEvent> selectSingleComponentHandler;

    // Association Event Handlers
    private EventHandler<MouseEvent> beginResizeAssociationHandler;
    private EventHandler<MouseEvent> resizeAssociationNWHandler;
    private EventHandler<MouseEvent> resizeAssociationSEHandler;
    private EventHandler<MouseEvent> beginHoverAssociationHandler;
    private EventHandler<MouseEvent> endHoverAssociationHandler;
    private EventHandler<MouseEvent> beginAssociationTextDragHandler;
    private EventHandler<MouseEvent> dragAssociationTextHandler;
    private EventHandler<MouseEvent> consumeAssociationClicksHandler;

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

    public void addAssociationIcon(AssociationIcon icon) {
        canvas.associationGroup.getChildren().add(icon.getAssociationGroup());
        // install the event handlers on individual association components

        // disables the ghost when hovering over an association
        icon.getAssociationGroup().addEventHandler(MouseEvent.MOUSE_ENTERED, beginHoverAssociationHandler);
        icon.getAssociationGroup().addEventHandler(MouseEvent.MOUSE_EXITED, endHoverAssociationHandler);

        // prevents associations from being made inside an association
        icon.getRect().addEventHandler(MouseEvent.MOUSE_PRESSED, consumeAssociationClicksHandler);

        // the event handlers for the border lines (allows you to drag them)
        icon.getHandles().forEach(handle -> {
            handle.addEventHandler(MouseEvent.MOUSE_PRESSED, beginResizeAssociationHandler);
            setCursorChange(handle, Cursor.NW_RESIZE);
        });
        icon.getResizeHandleNW().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeAssociationNWHandler);
        icon.getResizeHandleSE().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeAssociationSEHandler);

        // event handlers for the text (allows you to drag it)
        Text text = icon.getText();
        text.addEventHandler(MouseEvent.MOUSE_PRESSED, beginAssociationTextDragHandler);
        text.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragAssociationTextHandler);

        // cursor changes on hover
        setCursorChange(text, Cursor.MOVE);
    }

    public void showBackgroundGrid(boolean show) {
        if (show) {
            canvas.backgroundGrid.setOpacity(1);
        } else {
            canvas.backgroundGrid.setOpacity(0);
        }
    }

    private void setCursorChange(Node node, Cursor cursor) {
        node.setOnMouseEntered(event -> canvas.setCursor(cursor));
        node.setOnMouseExited(event -> canvas.setCursor(Cursor.DEFAULT));
        node.setOnDragDetected(event -> canvas.setCursor(Cursor.CLOSED_HAND));
        node.setOnMouseReleased(event -> canvas.setCursor(Cursor.DEFAULT));
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

    public void setBeginResizeAssociationHandler(EventHandler<MouseEvent> beginResizeAssociationHandler) {
        this.beginResizeAssociationHandler = beginResizeAssociationHandler;
    }

    public void setEndHoverAssociationHandler(EventHandler<MouseEvent> endHoverAssociationHandler) {
        this.endHoverAssociationHandler = endHoverAssociationHandler;
    }

    public void setBeginAssociationTextDragHandler(EventHandler<MouseEvent> beginAssociationTextDragHandler) {
        this.beginAssociationTextDragHandler = beginAssociationTextDragHandler;
    }

    public void setDragAssociationTextHandler(EventHandler<MouseEvent> dragAssociationTextHandler) {
        this.dragAssociationTextHandler = dragAssociationTextHandler;
    }

    public void setResizeAssociationNWHandler(EventHandler<MouseEvent> resizeAssociationNWHandler) {
        this.resizeAssociationNWHandler = resizeAssociationNWHandler;
    }

    public void setResizeAssociationSEHandler(EventHandler<MouseEvent> resizeAssociationSEHandler) {
        this.resizeAssociationSEHandler = resizeAssociationSEHandler;
    }

    public void setBeginHoverAssociationHandler(EventHandler<MouseEvent> beginHoverAssociationHandler) {
        this.beginHoverAssociationHandler = beginHoverAssociationHandler;
    }

    public void setConsumeAssociationClicksHandler(EventHandler<MouseEvent> consumeAssociationClicksHandler) {
        this.consumeAssociationClicksHandler = consumeAssociationClicksHandler;
    }
}
