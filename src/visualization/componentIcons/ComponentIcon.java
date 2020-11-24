package visualization.componentIcons;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import model.geometry.Point;
import visualization.GridScene;

public class ComponentIcon {

    private final Rectangle boundingRect = new Rectangle();
    private final Group iconNode = new Group();
    private final Group componentNode = new Group(iconNode, boundingRect);
    private final Group energyOutlineNodes = new Group();

    public ComponentIcon() {
        iconNode.setMouseTransparent(true);
        energyOutlineNodes.setMouseTransparent(true);
    }

    public void setComponentIconID(String id) {
        boundingRect.setId(id);
    }

    public void setBoundingRect(Point position, int unitWidth, int unitHeight) {
        Point topLeft = position.translate(-(unitWidth * GridScene.UNIT)/2, 0);
        boundingRect.setX(topLeft.getX());
        boundingRect.setY(topLeft.getY());
        boundingRect.setWidth(unitWidth * GridScene.UNIT);
        boundingRect.setHeight(unitHeight * GridScene.UNIT);
        boundingRect.setFill(Color.TRANSPARENT);
        boundingRect.setStroke(Color.TRANSPARENT);
    }

    protected void addShapesToEnergyOutlineNode(Group energyOutlineNode, Shape... shapes) {
        for (Shape shape : shapes) {
            Shape energyOutlineShape = ShapeCopier.copyShape(shape);
            energyOutlineShape.setStrokeType(StrokeType.CENTERED);
            energyOutlineShape.setStrokeWidth(GridScene.ENERGY_STROKE_WIDTH);
            energyOutlineShape.setStroke(Color.YELLOW);
            energyOutlineShape.setFill(Color.TRANSPARENT);
            // apply transforms to copy
            shape.getTransforms().forEach(transform -> energyOutlineShape.getTransforms().add(transform));

            energyOutlineNode.getChildren().add(energyOutlineShape);
        }
    }

    public void addNodesToIconNode(Node... nodes) {
        iconNode.getChildren().addAll(nodes);
    }

    public void addEnergyOutlineNode(Node... nodes) {
        energyOutlineNodes.getChildren().addAll(nodes);
    }

    public Group getComponentNode() {
        return componentNode;
    }

    public Group getEnergyOutlineNodes() {
        return energyOutlineNodes;
    }
}
