package visualization;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.List;

public class SourceIcon extends Group {

    private final Group sourceNode = new Group();
    private final Group sourceNodeEnergyOutline = new Group();

    private final List<Line> outputLineEnergyOutlines = new ArrayList<>();

    public SourceIcon() {
        getChildren().addAll(sourceNodeEnergyOutline, sourceNode);
    }

    // could create a base class component icon and add this function there
    public void addSourceNodeShapes(Shape... shapes) {
        for (Shape shape: shapes) {
            sourceNode.getChildren().add(shape);

            Shape energyOutlineShape = ShapeCopier.copyShape(shape);
            energyOutlineShape.setStrokeType(StrokeType.CENTERED);
            energyOutlineShape.setStrokeWidth(ComponentIconCreator.ENERGY_STROKE_WIDTH);
            energyOutlineShape.setStroke(Color.YELLOW);
            energyOutlineShape.setFill(Color.TRANSPARENT);
            // apply transforms to copy
            shape.getTransforms().forEach(transform -> energyOutlineShape.getTransforms().add(transform));

            sourceNodeEnergyOutline.getChildren().add(energyOutlineShape);
        }
    }

    public void addOutputLine(Line line) {
        Line energyOutlineLine = (Line)ShapeCopier.copyShape(line);
        energyOutlineLine.setStrokeWidth(ComponentIconCreator.ENERGY_STROKE_WIDTH);
        energyOutlineLine.setStroke(Color.YELLOW);

        outputLineEnergyOutlines.add(energyOutlineLine);

        getChildren().addAll(energyOutlineLine, line);
    }

    public void setSourceNodeEnergyState(boolean energized) {
        sourceNodeEnergyOutline.getChildren().forEach(element -> element.setOpacity(energized ? 1 : 0));
    }

    public void setWireEnergyState(boolean energized, int index) {
        outputLineEnergyOutlines.get(index).setOpacity(energized ? 1 : 0);
    }


}
