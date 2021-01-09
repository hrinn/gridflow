package construction.canvas;

import application.Globals;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class GridCanvas extends Pane {

    private final DoubleProperty scale = new SimpleDoubleProperty(1.0);

    private final double unitWidth = 1000;
    private final double unitHeight = 500;

    public final Group componentGroup = new Group();
    public final Group energyOutlineGroup = new Group();
    public final Group boundingRectGroup = new Group();
    public final Group overlayGroup = new Group();
    public final Group backgroundGrid = new Group();
    public final Group associationGroup = new Group();

    public GridCanvas() {
        getChildren().addAll(backgroundGrid, energyOutlineGroup, componentGroup, associationGroup, boundingRectGroup, overlayGroup);
        addBackgroundGrid();

        setPrefSize(unitWidth * Globals.UNIT, unitHeight * Globals.UNIT);
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

    private void addBackgroundGrid() {
        double width = unitWidth * Globals.UNIT;
        double height = unitHeight * Globals.UNIT;
        // vertical lines
        for (int i = 0; i < width; i += Globals.UNIT) {
            Line gridLine = new Line(i, 0, i, height);
            gridLine.setStroke(Color.LIGHTGRAY);
            gridLine.setOpacity(0.5);
            backgroundGrid.getChildren().add(gridLine);
        }

        // horizontal lines
        for (int i = 0; i < height; i += Globals.UNIT) {
            Line gridLine = new Line(0, i, width, i);
            gridLine.setStroke(Color.LIGHTGRAY);
            gridLine.setOpacity(0.5);
            backgroundGrid.getChildren().add(gridLine);
        }
    }
}
