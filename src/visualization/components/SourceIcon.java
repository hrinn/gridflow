package visualization.components;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import visualization.ShapeCopier;

import java.util.ArrayList;
import java.util.List;

public class SourceIcon extends ComponentIcon {

    private final Group sourceNode = new Group();
    private final Group sourceNodeEnergyOutline = new Group();

    private final List<Line> outputLineEnergyOutlines = new ArrayList<>();

    public SourceIcon() {
        getChildren().addAll(sourceNodeEnergyOutline, sourceNode);
    }

    // could create a base class component icon and add this function there
    public void addSourceNodeShapes(Shape... shapes) {
        addNodeShapes(sourceNode, sourceNodeEnergyOutline, shapes);
    }

    public void addOutputLine(Line line) {
        Line energyOutlineLine = (Line)ShapeCopier.copyShape(line);
        energyOutlineLine.setStrokeWidth(ComponentIconCreator.ENERGY_STROKE_WIDTH);
        energyOutlineLine.setStroke(Color.YELLOW);

        outputLineEnergyOutlines.add(energyOutlineLine);

        getChildren().addAll(energyOutlineLine, line);
        line.toBack();
        energyOutlineLine.toBack();
    }

    public void setSourceNodeEnergyState(boolean energized) {
        sourceNodeEnergyOutline.getChildren().forEach(element -> element.setOpacity(energized ? 1 : 0));
    }

    public void setWireEnergyState(boolean energized, int index) {
        outputLineEnergyOutlines.get(index).setOpacity(energized ? 1 : 0);
    }


}
