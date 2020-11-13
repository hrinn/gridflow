package visualization;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Grid;
import model.components.Component;
import model.components.IToggleable;
import model.components.Wire;

import java.util.UUID;

public class GraphDisplay {

    private Group nodes;
    private Group edges;
    private Group graphRoot;
    private Grid grid;

    public GraphDisplay(Grid grid) {
        nodes = new Group();
        edges = new Group();
        graphRoot = new Group(edges, nodes);
        this.grid = grid;

        EventHandler<MouseEvent> nodeClickedHandler = mouseEvent -> handleNodeClicked(mouseEvent);
        nodes.addEventFilter(MouseEvent.MOUSE_CLICKED, nodeClickedHandler);
    }

    private void handleNodeClicked(MouseEvent mouseEvent) {
        Parent targetNode = ((Node)mouseEvent.getTarget()).getParent();

        Component component = grid.getComponent(targetNode.getId());

        if (component instanceof IToggleable) {
            ((IToggleable)component).toggleState();
            updateComponent(component); // TODO: This should be moved, update component should not be called directly by click
        }
    }


    public Group getGraphRoot() {
        return graphRoot;
    }

    private void addTextElement(Group node, Text text) {
        node.getChildren().add(text);
    }

    private void addNodeElement(Group node) {
        nodes.getChildren().add(node);
    }

    private void addEdgeElement(Line edge) {
        edges.getChildren().add(edge);
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

        addEdgeElement(line);
    }

    private void addComponent(Component component) {
        // base ellipse
        Ellipse ellipse = new Ellipse();

        ellipse.setCenterX(component.getPosition().getX());
        ellipse.setCenterY(component.getPosition().getY());
        ellipse.setRadiusX(30);
        ellipse.setRadiusY(20);
        ellipse.setStroke(Color.BLACK);
        ellipse.setFill(Color.WHITE);

        Text name = new Text();
        name.setText(component.getName());
        name.setX(ellipse.getCenterX());
        name.setY(ellipse.getCenterY());
        name.setTextAlignment(TextAlignment.RIGHT);

        Group node = new Group(ellipse, name);
        node.setId(component.getId().toString());
        addNodeElement(node);

        // only display if it can be toggled
        if (component instanceof IToggleable) {
            Text state = new Text();
            state.setText(((IToggleable) component).getState() ? "true" : "false");
            state.setX(ellipse.getCenterX());
            state.setY(ellipse.getCenterY() + 10);
            state.setTextAlignment(TextAlignment.LEFT);
            addTextElement(node, state);
        }

    }

    private void deleteComponent(UUID id) {
        Node node = nodes.getChildren().stream()
                .filter(node1 -> node1.getId().equals(id.toString()))
                .findFirst().orElse(null);
        if (node == null) return;
        nodes.getChildren().remove(node);
    }

    private void updateComponent(Component component) {
        deleteComponent(component.getId());
        addComponent(component);
    }

}
