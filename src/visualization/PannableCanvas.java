package visualization;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PannableCanvas extends Pane {

    DoubleProperty scale = new SimpleDoubleProperty(1.0);

    public PannableCanvas() {
        setPrefSize(10000, 5000);
        setStyle("-fx-background-color: white; -fx-border-color: blue;");

        scaleXProperty().bind(scale);
        scaleYProperty().bind(scale);
    }

    public void addGrid() {

        double width = getBoundsInLocal().getWidth();
        double height = getBoundsInLocal().getHeight();

        Canvas grid = new Canvas(width, height);

        // don't catch mouse events
        grid.setMouseTransparent(true);

        GraphicsContext graphicsContext = grid.getGraphicsContext2D();

        graphicsContext.setStroke(Color.GRAY);
        graphicsContext.setLineWidth(1);

        // draw grid lines
        double offset = 50;
        for (double i=offset; i < width; i+=offset) {
            graphicsContext.strokeLine( i, 0, i, height);
            graphicsContext.strokeLine( 0, i, width, i);
        }

        getChildren().add(grid);

        grid.toBack();
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
}
