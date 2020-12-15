package construction.canvas;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.WireIcon;

import java.util.ArrayList;
import java.util.List;

public class GridCanvasMaster {

    // this is the master's baby. it adds nodes to and removes nodes from the canvas.
    private GridCanvas canvas;

    private final List<ComponentIcon> componentIcons = new ArrayList<>();

    // Component Event Handlers
    private EventHandler<MouseEvent> toggleComponentEventHandler;
    private EventHandler<MouseEvent> enterComponentHoverEventHandler;
    private EventHandler<MouseEvent> exitComponentHoverEventHandler;

    public GridCanvasMaster() {
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
        canvas.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
    }

    public void addComponentIcon(ComponentIcon icon) {
        Group componentNode = icon.getComponentNode();
        Group energyOutlineNodes = icon.getEnergyOutlineNodes();
        componentNode.addEventHandler(MouseEvent.MOUSE_PRESSED, toggleComponentEventHandler);
        if (!(icon instanceof WireIcon)) {
            componentNode.setOnMouseEntered(enterComponentHoverEventHandler);
            componentNode.setOnMouseExited(exitComponentHoverEventHandler);
        }

        canvas.componentGroup.getChildren().add(componentNode);
        canvas.energyOutlineGroup.getChildren().add(energyOutlineNodes);
        componentIcons.add(icon);
    }

    public void addGhostIcon(ComponentIcon ghostIcon) {
        canvas.ghostGroup.getChildren().add(ghostIcon.getComponentNode());
    }

    public void removeGhostIcon() {
        canvas.ghostGroup.getChildren().clear();
    }

    public void clearComponentGroups() {
        canvas.componentGroup.getChildren().clear();
        canvas.energyOutlineGroup.getChildren().clear();
    }

    public void setCanvasCursor(Cursor cursor) {
        canvas.setCursor(cursor);
    }

    public void addCanvasEventHandler(EventType eventType, EventHandler eventHandler) {
        canvas.addEventHandler(eventType, eventHandler);
    }

    public void addCanvasEventFilter(EventType eventType, EventHandler eventHandler) {
        canvas.addEventFilter(eventType, eventHandler);
    }

    public void getComponentIconByID(String ID) {
        componentIcons.forEach(icon -> {
            if (icon.getID().equals(ID)) {
                icon.selectComponent();
            }
        });
    }

    public GridCanvas getCanvas() {
        return canvas;
    }

    public void setToggleComponentEventHandler(EventHandler<MouseEvent> eventHandler) {
        this.toggleComponentEventHandler = eventHandler;
    }

    public void setEnterComponentHoverEventHandler(EventHandler<MouseEvent> eventHandler) {
        this.enterComponentHoverEventHandler = eventHandler;
    }

    public void setExitComponentHoverEventHandler(EventHandler<MouseEvent> eventHandler) {
        this.exitComponentHoverEventHandler = eventHandler;
    }
}
