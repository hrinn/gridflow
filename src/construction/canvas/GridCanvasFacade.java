package construction.canvas;

import domain.geometry.Point;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import visualization.componentIcons.AssociationIcon;
import visualization.componentIcons.ComponentIcon;

public class GridCanvasFacade {

    // this is the master's baby. it adds nodes to and removes nodes from the canvas.
    private GridCanvas canvas;

    private Scene scene;

    // Component Event Handlers
    private EventHandler<MouseEvent> toggleComponentEventHandler;
    private EventHandler<MouseEvent> lockComponentEventHandler;
    private EventHandler<MouseEvent> selectSingleComponentHandler;

    // Association Event Handlers
    // Handlers to resize the association box using the handles on each corner
    private EventHandler<MouseEvent> beginResizeAssociationHandler;
    private EventHandler<MouseEvent> resizeAssociationNWHandler;
    private EventHandler<MouseEvent> resizeAssociationSEHandler;
    private EventHandler<MouseEvent> resizeAssociationNEHandler;
    private EventHandler<MouseEvent> resizeAssociationSWHandler;

    // Handlers to move the text and show new cursors
    private EventHandler<MouseEvent> beginAssociationTextDragHandler;
    private EventHandler<MouseEvent> dragAssociationTextHandler;
    private EventHandler<MouseEvent> showMoveCursorOnTextHoverHandler;
    private EventHandler<MouseEvent> showDefaultCursorOnLeaveTextHoverHandler;

    // general association events
    private EventHandler<MouseEvent> beginHoverAssociationHandler;
    private EventHandler<MouseEvent> endHoverAssociationHandler;
    private EventHandler<MouseEvent> consumeAssociationClicksHandler;

    public GridCanvasFacade(Scene scene) {
        this.scene = scene;
        createCanvas();
    }

    private void createCanvas() {
        canvas = new GridCanvas();
        centerCanvas();

        // canvas events
        SceneGestures sceneGestures = new SceneGestures(canvas);

        // panning and scrolling
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, sceneGestures.getBeginPanEventHandler());
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnPanEventHandler());
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, sceneGestures.getEndPanEventHandler());
        canvas.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
    }

    public void centerCanvas() {
        setCameraPos((canvas.getPrefWidth()/2) / canvas.getScale(), (canvas.getPrefHeight()/2) / canvas.getScale());
    }

    // Takes an (x, y) in Grid Coordinates and sets the center of the camera there
    public void setCameraPos(double x, double y) {
        // Scaling makes translations... difficult. This is a hacky fix.
        canvas.setScale(1.0);

        double rx = -x + scene.getWidth()/2;
        double ry = -y + scene.getHeight()/2;
        canvas.setTranslateX(rx);
        canvas.setTranslateY(ry);
    }

    public void setTranslatePos(double x, double y) {
        canvas.setTranslateX(x);
        canvas.setTranslateY(y);
    }

    public void addComponentIcon(ComponentIcon icon) {
        Group componentNode = icon.getComponentNode();
        Group energyOutlineNodes = icon.getEnergyOutlineNodes();
        Rectangle boundingRect = icon.getBoundingRect();
        boundingRect.addEventHandler(MouseEvent.MOUSE_PRESSED, toggleComponentEventHandler);
        boundingRect.addEventHandler(MouseEvent.MOUSE_PRESSED, lockComponentEventHandler);
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
        });
        icon.getResizeHandleNW().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeAssociationNWHandler);
        setHandleCursorChange(icon.getResizeHandleNW(), Cursor.NW_RESIZE);
        icon.getResizeHandleSE().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeAssociationSEHandler);
        setHandleCursorChange(icon.getResizeHandleSE(), Cursor.SE_RESIZE);
        icon.getResizeHandleNE().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeAssociationNEHandler);
        setHandleCursorChange(icon.getResizeHandleNE(), Cursor.NE_RESIZE);
        icon.getResizeHandleSW().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeAssociationSWHandler);
        setHandleCursorChange(icon.getResizeHandleSW(), Cursor.SW_RESIZE);

        // event handlers for the text (allows you to drag it)
        Text text = icon.getText();
        text.addEventHandler(MouseEvent.MOUSE_PRESSED, beginAssociationTextDragHandler);
        text.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragAssociationTextHandler);
        text.addEventHandler(MouseEvent.MOUSE_ENTERED, showMoveCursorOnTextHoverHandler);
        text.addEventHandler(MouseEvent.MOUSE_EXITED, showDefaultCursorOnLeaveTextHoverHandler);
    }

    public void showBackgroundGrid(boolean show) {
        if (show) {
            canvas.backgroundGrid.setOpacity(1);
        } else {
            canvas.backgroundGrid.setOpacity(0);
        }
    }

    private void setHandleCursorChange(Node handle, Cursor cursor) {
        handle.setOnMouseEntered(event -> {
            if (((Node)event.getTarget()).getOpacity() != 0) {
                canvas.setCursor(cursor);
            }
        });
        handle.setOnMouseExited(event -> canvas.setCursor(Cursor.DEFAULT));
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

    // All these setters all the event handlers to be set from the Construction Controller
    public void setToggleComponentEventHandler(EventHandler<MouseEvent> eventHandler) {
        this.toggleComponentEventHandler = eventHandler;
    }

    public void setLockComponentEventHandler(EventHandler<MouseEvent> eventHandler) {
        this.lockComponentEventHandler = eventHandler;
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

    public void setResizeAssociationNEHandler(EventHandler<MouseEvent> resizeAssociationNEHandler) {
        this.resizeAssociationNEHandler = resizeAssociationNEHandler;
    }

    public void setResizeAssociationSWHandler(EventHandler<MouseEvent> resizeAssociationSWHandler) {
        this.resizeAssociationSWHandler = resizeAssociationSWHandler;
    }

    public void setShowDefaultCursorOnLeaveTextHoverHandler(EventHandler<MouseEvent> showDefaultCursorOnLeaveTextHoverHandler) {
        this.showDefaultCursorOnLeaveTextHoverHandler = showDefaultCursorOnLeaveTextHoverHandler;
    }

    public void setShowMoveCursorOnTextHoverHandler(EventHandler<MouseEvent> showMoveCursorOnTextHoverHandler) {
        this.showMoveCursorOnTextHoverHandler = showMoveCursorOnTextHoverHandler;
    }
}
