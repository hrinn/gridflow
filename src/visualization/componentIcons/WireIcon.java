package visualization.componentIcons;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
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

}
