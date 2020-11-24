package visualization.componentIcons;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.List;

public class WireIcon extends ComponentIcon {

    private Shape wireShape;
    private Shape energyOutline;

    public WireIcon() {
    }

    @Override
    public List<Node> getNodes() {
        return List.of(wireShape);
    }

    @Override
    public List<Node> getEnergyOutlineNodes() {
        return List.of(energyOutline);
    }

    public void addWireShape(Shape wireShape) {
        this.wireShape = wireShape;

        energyOutline = ShapeCopier.copyShape(wireShape);
        energyOutline.setStrokeWidth(ComponentIconCreator.ENERGY_STROKE_WIDTH);
        energyOutline.setStroke(Color.YELLOW);
        energyOutline.setFill(Color.TRANSPARENT);
    }

    public void setWireIconEnergyState(boolean energized) {
        energyOutline.setOpacity(energized ? 1 : 0);
    }

}
