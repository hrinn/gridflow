package visualization.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import visualization.ShapeCopier;

public class WireIcon extends ComponentIcon {

    private Shape energyOutline;

    public WireIcon() {
    }

    public void addWireShape(Shape wireShape) {
        energyOutline = ShapeCopier.copyShape(wireShape);
        energyOutline.setStrokeWidth(ComponentIconCreator.ENERGY_STROKE_WIDTH);
        energyOutline.setStroke(Color.YELLOW);
        energyOutline.setFill(Color.TRANSPARENT);
        getChildren().addAll(energyOutline, wireShape);
    }

    public void setWireIconEnergyState(boolean energized) {
        energyOutline.setOpacity(energized ? 1 : 0);
    }

}
