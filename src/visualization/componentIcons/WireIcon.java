package visualization.componentIcons;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import domain.geometry.Point;
import visualization.GridVisualizer;

public class WireIcon extends ComponentIcon {

    private Shape energyOutline;

    public WireIcon() {
    }

    public void addWireShape(Shape wireShape) {
        energyOutline = ShapeCopier.copyShape(wireShape);
        energyOutline.setStrokeWidth(GridVisualizer.ENERGY_STROKE_WIDTH);
        energyOutline.setStroke(Color.YELLOW);
        energyOutline.setFill(Color.TRANSPARENT);


        addNodesToIconNode(wireShape);
        addEnergyOutlineNode(energyOutline);
    }

    public void setWireIconEnergyState(boolean energized) {
        energyOutline.setOpacity(energized ? 1 : 0);
    }

    @Override
    public void setBoundingRect(Point position, double unitWidth, double unitHeight, double unitWidthPadding, double unitHeightPadding) {
        double width = unitWidth * GridVisualizer.UNIT;
        double height = unitHeight * GridVisualizer.UNIT;
        double widthPadding = unitWidthPadding * GridVisualizer.UNIT;
        double heightPadding = unitHeightPadding * GridVisualizer.UNIT;

        setBoundingRectParametersByCenter(position, width + widthPadding, height + heightPadding);
    }
}
