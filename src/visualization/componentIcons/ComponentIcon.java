package visualization.componentIcons;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import model.geometry.Point;
import visualization.GridScene;

import java.util.Arrays;

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

    public void setBoundingRect(Point position, double unitWidth, double unitHeight, double unitWidthPadding, double unitHeightPadding) {
        double width = unitWidth * GridScene.UNIT;
        double height = unitHeight * GridScene.UNIT;
        double widthPadding = unitWidthPadding * GridScene.UNIT;
        double heightPadding = unitHeightPadding * GridScene.UNIT;

        Point center = position.translate(0, height/2);
        setBoundingRectParametersByCenter(center, width + widthPadding, height + heightPadding);
    }

    protected void setBoundingRectParametersByCenter(Point center, double width, double height) {
        Point topLeft = center.translate(-width/2, -height/2);
        boundingRect.setX(topLeft.getX());
        boundingRect.setY(topLeft.getY());
        boundingRect.setWidth(width);
        boundingRect.setHeight(height);
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

    protected Rectangle getBoundingRect() {
        return boundingRect;
    }
}
