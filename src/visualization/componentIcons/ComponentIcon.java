package visualization.componentIcons;

import application.Globals;
import construction.ComponentType;
import javafx.animation.StrokeTransition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import domain.geometry.Point;
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
    private boolean activeLeft;
    private Point currentNamePos;
    private final Rectangle fittingRect = new Rectangle();
    private final Group iconNode = new Group();
    private final Text componentName = new Text();
    private final Group componentNode = new Group(iconNode, componentName);
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
        setComponentNamePosition(midRight);
        this.currentNamePos = midRight;
        this.activeLeft = false;
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

    public Point getCurrentNamePos () { return this.currentNamePos; }

    public void setCurrentNamePos (Point pos) { this.currentNamePos = pos; }

    public boolean getActiveLeft () { return this.activeLeft; }

    public void setActiveLeft (boolean isLeft) { this.activeLeft = isLeft; }

    public Point getMidRight() { return this.midRight; }

    public Point getMidLeft() { return this.midLeft; }

    public void updateComponentNamePosition (ComponentType type, boolean toggled, Double rotation) {
        Double X = 0.0;
        Double Y = 0.0;

        if (currentNamePos != null) {
            if (type == ComponentType.POWER_SOURCE) {
                // always set the name to center
                if (rotation == 180) {
                    X = this.midLeft.getX() + (0.45*Globals.UNIT);
                    Y = this.midLeft.getY() - (3.25*Globals.UNIT);
                } else {
                    X = this.midLeft.getX() + (0.3*Globals.UNIT);
                    Y = this.midLeft.getY() - (3.45*Globals.UNIT);
                }
                setComponentNamePosition(new Point(X, Y));
                this.currentNamePos = new Point(X, Y);

            } else if (type == ComponentType.TURBINE) {
                // always set the name to center
                if (rotation == 180) {
                    X = this.midLeft.getX() + (0.65*Globals.UNIT);
                    Y = this.midLeft.getY() + (0.35*Globals.UNIT);
                } else {
                    X = this.midLeft.getX() + (0.65*Globals.UNIT);
                    Y = this.midLeft.getY() + (0.25*Globals.UNIT);
                }
                setComponentNamePosition(new Point(X, Y));
                this.currentNamePos = new Point(X, Y);
            } else {
                // normal components
                if (toggled) {
                    // set name to "midLeft"
                    if (rotation == 90) {
                        X = this.midRight.getX() - (0.25*Globals.UNIT);
                        Y = this.midRight.getY() + (0.3*Globals.UNIT);
                    } else if (rotation == 270) {
                        X = this.midLeft.getX() - Globals.UNIT;
                        Y = this.midLeft.getY() + (0.25*Globals.UNIT);
                    } else if (rotation == 180) {
                        // Adjust for upside down text
                        X = this.midRight.getX();
                        Y = this.midRight.getY() + (0.55 * Globals.UNIT);
                    } else {
                        // Adjust for left justified text
                        X = this.midLeft.getX() - (1.25 * Globals.UNIT);
                        Y = this.midLeft.getY();
                    }

                    setComponentNamePosition(new Point(X, Y));
                    this.currentNamePos = new Point(X, Y);
                    this.activeLeft = true;

                } else {
                    // set name to "midRight"
                    if (rotation == 90) {
                        X = this.midLeft.getX() - Globals.UNIT;
                        Y = this.midLeft.getY() + (0.25*Globals.UNIT);
                    } else if (rotation == 270) {
                        X = this.midRight.getX() - (0.25*Globals.UNIT);
                        Y = this.midRight.getY() + (0.3*Globals.UNIT);
                    }
                    else if (rotation == 180) {
                        X = this.midLeft.getX() - (1.25 * Globals.UNIT);
                        Y = this.midLeft.getY() + (0.55 * Globals.UNIT);
                    } else {
                        X = this.midRight.getX();
                        Y = this.midRight.getY();
                    }

                    setComponentNamePosition(new Point(X, Y));
                    this.currentNamePos = new Point(X, Y);
                    this.activeLeft = false;
                }
            }
        }
    }

    public void setComponentNamePosition(Point position) {
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
