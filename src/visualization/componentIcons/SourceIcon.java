package visualization.componentIcons;

import application.Globals;
import construction.ComponentType;
import domain.geometry.Point;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class SourceIcon extends ComponentIcon {

    private final Group sourceNodeEnergyOutline = new Group();
    private final List<Line> outputEnergyOutlines = new ArrayList<>();
    private ComponentType type;

    public SourceIcon(ComponentType type) {
        this.type = type;
        addEnergyOutlineNode(sourceNodeEnergyOutline);
    }

    public void addSourceNodeShapes(Shape... shapes) {
        addNodesToIconNode(shapes);
        addShapesToEnergyOutlineNode(sourceNodeEnergyOutline, shapes);
    }

    public void addStaticNodeShapes(Node... nodes) {
        addNodesToIconNode(nodes);
    }

    @Override
    public void setComponentNamePosition(boolean none) {
        AnchorPane positioner = getComponentNamePositioner();
        Text text = getComponentName();

        AnchorPane.clearConstraints(text);
        AnchorPane.setRightAnchor(text, positioner.getPrefWidth()/2 - text.prefWidth(-1)/2);
        text.setTextAlignment(TextAlignment.CENTER);

        if (type == ComponentType.POWER_SOURCE) {
            AnchorPane.setTopAnchor(text, positioner.getPrefHeight()/3 - text.prefHeight(-1)/3);
        } else {
            // Turbine
            // TODO: adjust the top anchor shift to be more center
            AnchorPane.setTopAnchor(text, positioner.getPrefHeight()/2 - text.prefHeight(-1) * .4);
        }
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
