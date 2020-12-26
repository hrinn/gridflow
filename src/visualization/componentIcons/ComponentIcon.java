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
    private final Rectangle fittingRect = new Rectangle();
    private final Group iconNode = new Group();
    private final Text componentName = new Text();
    private final Group componentNode = new Group(iconNode, componentName);
    private final Group energyOutlineNodes = new Group();
    private double height;

    private final static Color SELECT_COLOR = Color.BLUE;
    public final static Color DEFAULT_BOUNDING_COLOR = Color.TRANSPARENT;
    private final static Color DEFAULT_FITTING_COLOR = Color.TRANSPARENT;

    public ComponentIcon() {
        energyOutlineNodes.setMouseTransparent(true);
        componentNode.setMouseTransparent(true);
        fittingRect.setMouseTransparent(true);

        boundingRect.setFill(Color.TRANSPARENT);
        boundingRect.setStroke(DEFAULT_BOUNDING_COLOR);
        boundingRect.setOpacity(0.5);

        fittingRect.setFill(Color.TRANSPARENT);
        fittingRect.setStroke(DEFAULT_FITTING_COLOR);
        fittingRect.setOpacity(0.5);
    }

    public void setComponentIconID(String id) {
        boundingRect.setId(id);
    }

    public String getID() {
        return boundingRect.getId();
    }

    public void setBoundingRect(Dimensions dimensions, Point position) {
        this.height = dimensions.getHeight();
        setRectByDimensions(boundingRect, dimensions, position);

        Point midRight = position.translate(dimensions.getAdjustedWidth()/2, dimensions.getAdjustedHeight()/2);
        setComponentNamePosition(midRight);
    }

    public void setFittingRect(Dimensions dimensions, Point position) {
        setRectByDimensions(fittingRect, dimensions, position);
    }

    private void setRectByDimensions(Rectangle rect, Dimensions dimensions, Point position) {
        double leftWidth = dimensions.getWidth()/2 + dimensions.getLeftPadding();
        Point topLeft = position.translate(-leftWidth, -dimensions.getTopPadding());
        rect.setX(topLeft.getX());
        rect.setY(topLeft.getY());

        rect.setWidth(dimensions.getAdjustedWidth());
        rect.setHeight(dimensions.getAdjustedHeight());
    }

    public void setSelect(boolean select) {
        boundingRect.setStroke(select ? SELECT_COLOR : DEFAULT_BOUNDING_COLOR);
    }

    private void setComponentNamePosition(Point position) {
        double labelYShift = componentName.prefHeight(-1)/4;
        componentName.setLayoutX(position.getX());
        componentName.setLayoutY(position.getY() + labelYShift);
    }

    public void setComponentName(String name) {
        componentName.setText(name);
        componentName.setFont(Font.font(null, 10));
    }

    public void resetAngle() {
        componentNode.getTransforms().clear();
        energyOutlineNodes.getTransforms().clear();
        boundingRect.getTransforms().clear();
        fittingRect.getTransforms().clear();
    }

    public void setAngle(double angle, Point position) {
        Rotate rotateTransform = new Rotate();
        rotateTransform.setPivotX(position.getX());
        rotateTransform.setPivotY(position.getY());
        rotateTransform.setAngle(angle);
        componentNode.getTransforms().add(rotateTransform);
        boundingRect.getTransforms().add(rotateTransform);
        energyOutlineNodes.getTransforms().add(rotateTransform);
        fittingRect.getTransforms().add(rotateTransform);
    }

    public void setTranslate(double x, double y) {
        componentNode.setTranslateX(x);
        componentNode.setTranslateY(y);
        boundingRect.setTranslateX(x);
        boundingRect.setTranslateY(y);
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

    public Rectangle getFittingRect() {
        return fittingRect;
    }

    public double getHeight() {
        return height;
    }
}
