package visualization.componentIcons;

import application.Globals;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import domain.geometry.Point;

public class ComponentIcon {

    private final Rectangle boundingRect = new Rectangle();
    private final Group iconNode = new Group();
    private final Text componentName = new Text();
    private final Group componentNode = new Group(iconNode, componentName, boundingRect);
    private final Group energyOutlineNodes = new Group();

    public ComponentIcon() {
        iconNode.setMouseTransparent(true);
        energyOutlineNodes.setMouseTransparent(true);
        componentName.setMouseTransparent(true);
    }

    public void setComponentIconID(String id) {
        boundingRect.setId(id);
    }

    public void setBoundingRect(Point position, double unitWidth, double unitHeight, double unitWidthPadding, double unitHeightPadding) {
        double width = unitWidth * Globals.UNIT;
        double height = unitHeight * Globals.UNIT;

        // negative padding makes the clickable box a bit smaller than the actual unit size rectangle
        double widthPadding = unitWidthPadding * Globals.UNIT;
        double heightPadding = unitHeightPadding * Globals.UNIT;

        Point center = position.translate(0, height/2);
        setBoundingRectParametersByCenter(center, width + widthPadding, height + heightPadding);

        // set text position now that we know where it should go
        Point midRight = position.translate(width/2, height/2);
        setComponentNamePosition(midRight);
    }

    private void setComponentNamePosition(Point position) {
        componentName.setLayoutX(position.getX() - 5);
        componentName.setLayoutY(position.getY() + componentName.prefHeight(-1)/4);
    }

    public void setComponentName(String name) {
        String tabbedName = name;//.replace(' ', '\n');
        componentName.setText(tabbedName);
        componentName.setFont(Font.font(null, 10));
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
            energyOutlineShape.setStrokeWidth(Globals.ENERGY_STROKE_WIDTH);
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
