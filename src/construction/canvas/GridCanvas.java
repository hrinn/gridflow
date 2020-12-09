package construction.canvas;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import visualization.componentIcons.ComponentIcon;

public class GridCanvas extends Pane {

    private DoubleProperty scale = new SimpleDoubleProperty(1.0);
    private EventHandler<MouseEvent> toggleComponentEventHandler;

    public GridCanvas(EventHandler<MouseEvent> toggleComponentEventHandler) {
        // event handlers
        this.toggleComponentEventHandler = toggleComponentEventHandler;

        setPrefSize(12000, 6000);
        setStyle("-fx-background-color: white; -fx-border-color: blue;");

        scaleXProperty().bind(scale);
        scaleYProperty().bind(scale);
    }

    public double getScale() {
        return scale.get();
    }

    public void setScale(double scale) {
        this.scale.set(scale);
    }

    public void setPivot(double x, double y) {
        setTranslateX(getTranslateX() - x);
        setTranslateY(getTranslateY() - y);
    }

    public void addComponentIcon(ComponentIcon icon) {
        Group componentNode = icon.getComponentNode();
        Group energyOutlineNodes = icon.getEnergyOutlineNodes();
        componentNode.addEventFilter(MouseEvent.MOUSE_PRESSED, toggleComponentEventHandler);

        getChildren().addAll(energyOutlineNodes, componentNode);

        // all component nodes belong in front of energy outlines
        componentNode.toFront();
        energyOutlineNodes.toBack();
    }
}
