package visualization.componentIcons;

import application.Globals;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

public class SourceIcon extends ComponentIcon {

    private final Group sourceNodeEnergyOutline = new Group();
    private final List<Line> outputEnergyOutlines = new ArrayList<>();

    public SourceIcon() {
        addEnergyOutlineNode(sourceNodeEnergyOutline);
    }

    public void addSourceNodeShapes(Shape... shapes) {
        addNodesToIconNode(shapes);
        addShapesToEnergyOutlineNode(sourceNodeEnergyOutline, shapes);
    }

    public void addStaticNodeShapes(Node... nodes) {
        addNodesToIconNode(nodes);
    }

    public void addOutputLine(Line line) {
        Line energyOutline = (Line)ShapeCopier.copyShape(line);
        energyOutline.setStrokeWidth(Globals.ENERGY_STROKE_WIDTH);
        energyOutline.setStroke(Color.YELLOW);

        outputEnergyOutlines.add(energyOutline);

        addNodesToIconNode(line);
        addEnergyOutlineNode(energyOutline);
    }

    public void setSourceNodeEnergyState(boolean energized) {
        sourceNodeEnergyOutline.getChildren().forEach(element -> element.setOpacity(energized ? 1 : 0));
    }

    public void setWireEnergyState(boolean energized, int index) {
        outputEnergyOutlines.get(index).setOpacity(energized ? 1 : 0);
    }


}
