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
import javafx.scene.transform.Rotate;

public class ComponentIcon {

    private final Rectangle boundingRect = new Rectangle();
    private final Group iconNode = new Group();
    private final Text componentName = new Text();
    private final Group componentNode = new Group(iconNode, componentName);
    private final Group energyOutlineNodes = new Group();
    private double height;

    private final static Color SELECT_COLOR = Color.BLUE;
    private final static Color DEFAULT_BORDER_COLOR = Color.RED;

    public ComponentIcon() {
        energyOutlineNodes.setMouseTransparent(true);
        componentNode.setMouseTransparent(true);
        boundingRect.setFill(Color.TRANSPARENT);
        boundingRect.setStroke(DEFAULT_BORDER_COLOR);
        boundingRect.setOpacity(0.5);
    }

    public void setComponentIconID(String id) {
        boundingRect.setId(id);
    }

    public String getID() {
        return boundingRect.getId();
    }

    public void setBoundingRect(Dimensions dimensions, Point position) {
        this.height = dimensions.getHeight();
        Point topLeft = position.translate(-dimensions.getAdjustedWidth()/2, -dimensions.getTopPadding());
        boundingRect.setX(topLeft.getX());
        boundingRect.setY(topLeft.getY());

        boundingRect.setWidth(dimensions.getAdjustedWidth());
        boundingRect.setHeight(dimensions.getAdjustedHeight());

        Point midRight = position.translate(dimensions.getAdjustedWidth()/2, dimensions.getAdjustedHeight()/2);
        setComponentNamePosition(midRight);
    }

    public void select() {
        boundingRect.setStroke(SELECT_COLOR);
    }

    public void deSelect() {
        boundingRect.setStroke(DEFAULT_BORDER_COLOR);
    }

    private void setComponentNamePosition(Point position) {
        double labelYShift = componentName.prefHeight(-1)/4;
        componentName.setLayoutX(position.getX());
        componentName.setLayoutY(position.getY() + labelYShift);
    }

    public void setComponentName(String name) {
        String tabbedName = name;//.replace(' ', '\n');
        componentName.setText(tabbedName);
        componentName.setFont(Font.font(null, 10));
    }

    public void resetAngle() {
        componentNode.getTransforms().clear();
        energyOutlineNodes.getTransforms().clear();
        boundingRect.getTransforms().clear();
    }

    public void setAngle(double angle, Point position) {
        Rotate rotateTransform = new Rotate();
        rotateTransform.setPivotX(position.getX());
        rotateTransform.setPivotY(position.getY());
        rotateTransform.setAngle(angle);
        componentNode.getTransforms().add(rotateTransform);
        boundingRect.getTransforms().add(rotateTransform);
        energyOutlineNodes.getTransforms().add(rotateTransform);
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

    public Rectangle getBoundingRect() {
        return boundingRect;
    }

    public double getHeight() {
        return height;
    }
}
