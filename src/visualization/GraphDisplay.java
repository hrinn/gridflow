package visualization;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Grid;
import model.components.Component;
import model.components.Wire;

public class GraphDisplay {

    private Group root;

    public GraphDisplay() {
        root = new Group();
    }

    public Group getGraphRoot() {
        return root;
    }

    private void addElement(Node node) {
        root.getChildren().add(node);
    }

    public void displayGrid(Grid grid) {
        // draw connections
        for (Wire wire : grid.getWires()) {
            for (Component connection : wire.getConnections()) {
                addConnection(wire, connection);
            }
        }

        // draw components
        for (Component component : grid.getComponents()) {
            addComponent(component);
        }

    }

    public void addConnection(Component c1, Component c2) {
        Line line = new Line();

        line.setStartX(c1.getPosition().getX());
        line.setStartY(c1.getPosition().getY());

        line.setEndX(c2.getPosition().getX());
        line.setEndY(c2.getPosition().getY());

        addElement(line);
    }

    public void addComponent(Component component) {
        Ellipse graphcomp = new Ellipse();

        graphcomp.setCenterX(component.getPosition().getX());
        graphcomp.setCenterY(component.getPosition().getY());
        graphcomp.setRadiusX(30);
        graphcomp.setRadiusY(20);
        graphcomp.setStroke(Color.BLACK);
        graphcomp.setFill(Color.WHITE);

        addElement(graphcomp);

        Text text = new Text();
        text.setText(component.getName());
        text.setX(graphcomp.getCenterX());
        text.setY(graphcomp.getCenterY());
        text.setTextAlignment(TextAlignment.CENTER);

        addElement(text);
    }

}