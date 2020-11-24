package visualization.componentIcons;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

public class SourceIcon extends ComponentIcon {

    private final Group sourceNode = new Group();
    private final Group sourceNodeEnergyOutline = new Group();

    private final List<Line> outputLines = new ArrayList<>();
    private final List<Line> outputLineEnergyOutlines = new ArrayList<>();

    public SourceIcon() {
    }

    @Override
    public List<Node> getNodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(sourceNode);
        nodes.addAll(outputLines);
        return nodes;
    }

    @Override
    public List<Node> getEnergyOutlineNodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(sourceNodeEnergyOutline);
        nodes.addAll(outputLineEnergyOutlines);
        return nodes;
    }

    // could create a base class component icon and add this function there
    public void addSourceNodeShapes(Shape... shapes) {
        addShapesToNodeAndEnergyOutline(sourceNode, sourceNodeEnergyOutline, shapes);
    }

    public void addOutputLine(Line line) {
        Line energyOutlineLine = (Line)ShapeCopier.copyShape(line);
        energyOutlineLine.setStrokeWidth(ComponentIconCreator.ENERGY_STROKE_WIDTH);
        energyOutlineLine.setStroke(Color.YELLOW);

        outputLineEnergyOutlines.add(energyOutlineLine);
        outputLines.add(line);
    }

    public void setSourceNodeEnergyState(boolean energized) {
        sourceNodeEnergyOutline.getChildren().forEach(element -> element.setOpacity(energized ? 1 : 0));
    }

    public void setWireEnergyState(boolean energized, int index) {
        outputLineEnergyOutlines.get(index).setOpacity(energized ? 1 : 0);
    }


}
