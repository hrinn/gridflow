package visualization.components;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import visualization.ShapeCopier;

public class ComponentIcon extends Group {

    protected void addNodeShapes(Group node, Group energyOutline, Shape... shapes) {
        for (Shape shape : shapes) {
            node.getChildren().add(shape);

            Shape energyOutlineShape = ShapeCopier.copyShape(shape);
            energyOutlineShape.setStrokeType(StrokeType.CENTERED);
            energyOutlineShape.setStrokeWidth(ComponentIconCreator.ENERGY_STROKE_WIDTH);
            energyOutlineShape.setStroke(Color.YELLOW);
            energyOutlineShape.setFill(Color.TRANSPARENT);
            // apply transforms to copy
            shape.getTransforms().forEach(transform -> energyOutlineShape.getTransforms().add(transform));

            energyOutline.getChildren().add(energyOutlineShape);
        }
    }
}
