package visualization;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import model.components.Component;
import model.components.geometry.Point;

public class GraphDisplay {

    private Group root;

    public GraphDisplay() {
        root = new Group();
    }

    public Group getGraphRoot() {
        return root;
    }

    private void addNode(Node node) {
        root.getChildren().add(node);
    }

    public void addComponent(Point point) {
        Ellipse component = new Ellipse();

        component.setCenterX(point.getX());
        component.setCenterY(point.getY());
        component.setRadiusX(30);
        component.setRadiusY(20);
        component.setStroke(Color.BLACK);
        component.setFill(Color.WHITE);

        addNode(component);
    }

}
