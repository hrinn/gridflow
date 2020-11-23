package visualization;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import model.geometry.Point;

public class PannableCanvas extends Pane {

    private DoubleProperty scale = new SimpleDoubleProperty(1.0);

    public PannableCanvas(double x, double y) {
        setPrefSize(x, y);
        setStyle("-fx-background-color: white; -fx-border-color: blue;");

        scaleXProperty().bind(scale);
        scaleYProperty().bind(scale);
    }

    public Point getCenterPoint() {
        return new Point(getWidth()/2, getHeight()/2);
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
