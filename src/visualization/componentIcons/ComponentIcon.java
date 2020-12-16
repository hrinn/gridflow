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

    public String getID() {
        return boundingRect.getId();
    }

    public void setBoundingRect(domain.geometry.Rectangle boundingRectangle) {
        double tlx = boundingRectangle.getTopLeft().getX();
        double tly = boundingRectangle.getTopLeft().getY();

        boundingRect.setX(tlx);
        boundingRect.setY(tly);

        double width = Math.abs(tlx - boundingRectangle.getBottomRight().getX());
        double height = Math.abs(tly - boundingRectangle.getBottomRight().getY());

        boundingRect.setWidth(width);
        boundingRect.setHeight(height);
        boundingRect.setFill(Color.TRANSPARENT);
        boundingRect.setStroke(Color.TRANSPARENT);
        boundingRect.setOpacity(0.5);
        setComponentNamePosition(boundingRectangle.getMidRight());
    }

    public void select() {
        boundingRect.setStroke(Color.BLUE);
    }

    public void deSelect() {
        boundingRect.setStroke(Color.TRANSPARENT);
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
}
