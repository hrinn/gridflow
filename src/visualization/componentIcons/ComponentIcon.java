package visualization.componentIcons;

import application.Globals;
import construction.ComponentType;
import javafx.animation.StrokeTransition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import domain.geometry.Point;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ComponentIcon {

    private final Rectangle boundingRect = new Rectangle();
    private Point midRight;
    private Point midLeft;
    private final Rectangle fittingRect = new Rectangle();
    private final Group iconNode = new Group();
    private final Text componentName = new Text();
    private final AnchorPane componentNamePositioner = new AnchorPane();
    private final Group componentNode = new Group(iconNode);
    private final Group energyOutlineNodes = new Group();
    private double height;
    private final StrokeTransition errorTransition = new StrokeTransition(Duration.millis(1000), getBoundingRect(), Color.RED, DEFAULT_BOUNDING_COLOR);

    private List<Text> textElements = new ArrayList<>();

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

        this.midRight = position.translate(dimensions.getAdjustedWidth()/2, dimensions.getAdjustedHeight()/2);
        this.midLeft = position.translate(-(dimensions.getAdjustedWidth()/2), dimensions.getAdjustedHeight()/2);
    }


    public void setFittingRect(Dimensions dimensions, Point position) {
        setRectByDimensions(fittingRect, dimensions, position);
    }

    private void setRectByDimensions(Rectangle rect, Dimensions dimensions, Point position) {
        double leftWidth = dimensions.getWidth()/2 + dimensions.getLeftPadding();
        Point topLeft = position.translate(-leftWidth, -dimensions.getTopPadding());
        if(dimensions.isFlipped()) {
            topLeft = position.translate(-leftWidth,  - (dimensions.getHeight() + dimensions.getTopPadding()));
        }
        rect.setX(topLeft.getX());
        rect.setY(topLeft.getY());

        rect.setWidth(dimensions.getAdjustedWidth());
        rect.setHeight(dimensions.getAdjustedHeight());
    }

    public void setSelect(boolean select) {
        boundingRect.setStroke(select ? SELECT_COLOR : DEFAULT_BOUNDING_COLOR);
    }

    public Point getMidRight() { return this.midRight; }

    public Point getMidLeft() { return this.midLeft; }

    public void setComponentName(String name, boolean right) {
        if (name == null || name.isEmpty()) return;
        if (componentNamePositioner.getParent() == null) {
            // Add nodes to hierarchy
            componentNamePositioner.getChildren().add(componentName);
            componentNode.getChildren().add(componentNamePositioner);

            // Setup AnchorPane
            componentNamePositioner.setLayoutX(boundingRect.getX());
            componentNamePositioner.setLayoutY(boundingRect.getY());
            componentNamePositioner.setPrefWidth(boundingRect.getWidth());
            componentNamePositioner.setPrefHeight(boundingRect.getHeight());
            componentNamePositioner.setMaxWidth(boundingRect.getWidth());
            componentNamePositioner.setMaxHeight(boundingRect.getHeight());
            componentNamePositioner.setMinWidth(boundingRect.getWidth());
            componentNamePositioner.setMinHeight(boundingRect.getHeight());

            // Setup Text
            componentName.setWrappingWidth(Globals.UNIT * 2);
            componentName.setFont(Font.font(null, 10));
        }

        setComponentNamePosition(right);
        componentName.setText(name);
    }

    public void setComponentNamePosition(boolean right) {
        AnchorPane.clearConstraints(componentName);
        AnchorPane.setTopAnchor(componentName, componentNamePositioner.getPrefHeight()/2 - componentName.prefHeight(-1) * .4);
        double angle = -componentName.getRotate();

        if (right) {
            if (angle == 0) {
                // Name on right
                AnchorPane.setLeftAnchor(componentName, componentNamePositioner.getPrefWidth());
                componentName.setTextAlignment(TextAlignment.LEFT);

            } else if (angle == 90) {
                AnchorPane.setLeftAnchor(componentName, componentNamePositioner.getPrefWidth()/2);
                componentName.setTextAlignment(TextAlignment.CENTER);

            } else if (angle == 180) {
                // Name on left
                AnchorPane.setLeftAnchor(componentName, componentNamePositioner.getPrefWidth());
                componentName.setTextAlignment(TextAlignment.RIGHT);

            } else { // angle == 270
                AnchorPane.setLeftAnchor(componentName, componentNamePositioner.getPrefWidth()/2);
                componentName.setTextAlignment(TextAlignment.CENTER);

            }
        } else {
            if (angle == 0) {
                // Name on left
                AnchorPane.setRightAnchor(componentName, componentNamePositioner.getPrefWidth());
                componentName.setTextAlignment(TextAlignment.RIGHT);
            } else if (angle == 90) {
                AnchorPane.setRightAnchor(componentName, componentNamePositioner.getPrefWidth()/2);
                componentName.setTextAlignment(TextAlignment.CENTER);

            } else if (angle == 180) {
                // Name on right
                AnchorPane.setRightAnchor(componentName, componentNamePositioner.getPrefWidth());
                componentName.setTextAlignment(TextAlignment.LEFT);

            } else { // angle == 270
                AnchorPane.setRightAnchor(componentName, componentNamePositioner.getPrefWidth()/2);
                componentName.setTextAlignment(TextAlignment.CENTER);

            }
        }
    }

    public void resetAngle() {
        componentNode.getTransforms().clear();
        energyOutlineNodes.getTransforms().clear();
        boundingRect.getTransforms().clear();
        fittingRect.getTransforms().clear();
        textElements.forEach(text -> text.setRotate(0));
        componentName.setRotate(0);
    }

    public void setAngle(double angle, Point position) {
        // transform the rest of the components
        Rotate rotateTransform = new Rotate();
        rotateTransform.setPivotX(position.getX());
        rotateTransform.setPivotY(position.getY());
        rotateTransform.setAngle(angle);
        componentNode.getTransforms().add(rotateTransform);
        boundingRect.getTransforms().add(rotateTransform);
        energyOutlineNodes.getTransforms().add(rotateTransform);
        fittingRect.getTransforms().add(rotateTransform);
        textElements.forEach(text -> text.setRotate(-angle));
        componentName.setRotate(-angle);
    }

    public void setTranslate(double x, double y) {
        componentNode.setTranslateX(x);
        componentNode.setTranslateY(y);
        boundingRect.setTranslateX(x);
        boundingRect.setTranslateY(y);
    }

    public Point getTranslate() {
        return new Point(componentNode.getTranslateX(), componentNode.getTranslateY());
    }

    public void showError() {
        errorTransition.stop();
        errorTransition.play();
    }

    public void addTextElement(Text text) {
        textElements.add(text);
        iconNode.getChildren().add(text);
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

    protected void addComponentNamePositionerToComponentNode(AnchorPane componentNamePositioner) {
        componentNode.getChildren().add(componentNamePositioner);
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

    protected Text getComponentName() {
        return componentName;
    }

    protected AnchorPane getComponentNamePositioner() {
        return componentNamePositioner;
    }
}
