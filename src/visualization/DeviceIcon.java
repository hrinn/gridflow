package visualization;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

public class DeviceIcon extends Group {

    // TODO: add endpoint circles that don't get energized, can be clicked to add a component

    private static final double ENERGY_STROKE_WIDTH = 2;

    private final Group inNode = new Group();
    private final Group inNodeEnergyOutline = new Group();

    private final Group outNode = new Group();
    private final Group outNodeEnergyOutline = new Group();

    // the mid node is used when part of the component is only shown energized when both nodes are energized
    private final Group midNode = new Group();
    private final Group midNodeEnergyOutline = new Group();

    private final Group indicatorNode = new Group();

    public DeviceIcon() {
        getChildren().addAll(inNodeEnergyOutline, outNodeEnergyOutline, midNodeEnergyOutline,
                inNode, outNode, midNode, indicatorNode);
    }

    public void addInNodeShapes(Shape... shapes) {
        addNodeShapes(inNode, inNodeEnergyOutline, shapes);
    }

    public void addOutNodeShapes(Shape... shapes) {
        addNodeShapes(outNode, outNodeEnergyOutline, shapes);
    }

    public void addMidNodeShapes(Shape... shapes) {
        addNodeShapes(midNode, midNodeEnergyOutline, shapes);
    }

    private void addNodeShapes(Group node, Group energyOutline, Shape... shapes) {
        for (Shape shape : shapes) {
            node.getChildren().add(shape);

            Shape energyOutlineShape = ShapeCopier.copyShape(shape);
            energyOutlineShape.setStrokeType(StrokeType.OUTSIDE);
            energyOutlineShape.setStrokeWidth(ENERGY_STROKE_WIDTH);
            energyOutlineShape.setStroke(Color.YELLOW);
            energyOutlineShape.setFill(Color.WHITE);
            energyOutline.getChildren().add(energyOutlineShape);
        }
    }

    public void setNodeEnergyStates(boolean inNodeEnergized, boolean outNodeEnergized) {
        setInNodeEnergyState(inNodeEnergized);
        setOutNodeEnergyState(outNodeEnergized);

        if (!midNodeEnergyOutline.getChildren().isEmpty()) {
            setMidNodeEnergyState(inNodeEnergized && outNodeEnergized);
        }
    }

    private void setInNodeEnergyState(boolean energized) {
        inNodeEnergyOutline.getChildren().forEach(element -> element.setOpacity(energized ? 1 : 0));
    }

    private void setOutNodeEnergyState(boolean energized) {
        outNodeEnergyOutline.getChildren().forEach(element -> element.setOpacity(energized ? 1 : 0));
    }

    private void setMidNodeEnergyState(boolean energized) {
        midNodeEnergyOutline.getChildren().forEach(element -> element.setOpacity(energized ? 1 : 0));
    }
}
