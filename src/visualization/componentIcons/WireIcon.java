package visualization.componentIcons;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import model.geometry.Point;
import visualization.GridScene;

public class WireIcon extends ComponentIcon {

    private Shape energyOutline;

    public WireIcon() {
    }

    public void addWireShape(Shape wireShape) {
        energyOutline = ShapeCopier.copyShape(wireShape);
        energyOutline.setStrokeWidth(GridScene.ENERGY_STROKE_WIDTH);
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
        double width = unitWidth * GridScene.UNIT;
        double height = unitHeight * GridScene.UNIT;
        double widthPadding = unitWidthPadding * GridScene.UNIT;
        double heightPadding = unitHeightPadding * GridScene.UNIT;

        setBoundingRectParametersByCenter(position, width + widthPadding, height + heightPadding);
    }
}
