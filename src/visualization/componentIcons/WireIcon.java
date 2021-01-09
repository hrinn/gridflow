package visualization.componentIcons;

import application.Globals;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import domain.geometry.Point;

import java.util.List;

public class WireIcon extends ComponentIcon {

    private Group energyOutlines = new Group();

    public WireIcon() {
        addEnergyOutlineNode(energyOutlines);
    }

    public void addWireShape(Shape wireShape) {
        Shape energyOutline = ShapeCopier.copyShape(wireShape);
        energyOutline.setStrokeWidth(Globals.ENERGY_STROKE_WIDTH);
        energyOutline.setStroke(Color.YELLOW);
        energyOutline.setFill(Color.TRANSPARENT);

        addNodesToIconNode(wireShape);
        addShapesToEnergyOutlineNode(energyOutlines, wireShape);
    }

    public void setWireIconEnergyState(boolean energized) {
        energyOutlines.getChildren().forEach(child -> child.setOpacity(energized ? 1: 0));
    }

    @Override
    public void setBoundingRect(Dimensions dimensions, Point position) {
        dimensionsToWireRectangle(getBoundingRect(), dimensions, position);
    }

    @Override
    public void setFittingRect(Dimensions dimensions, Point position) {
        dimensionsToWireRectangle(getFittingRect(), dimensions, position);
    }

    private void dimensionsToWireRectangle(Rectangle rect, Dimensions dimensions, Point position) {
        Point topLeft = position.translate(-dimensions.getAdjustedWidth()/2, -dimensions.getAdjustedHeight()/2);
        rect.setX(topLeft.getX());
        rect.setY(topLeft.getY());
        rect.setWidth(dimensions.getAdjustedWidth());
        rect.setHeight(dimensions.getAdjustedHeight());
    }
}
