package model.components;

import model.geometry.*;

import java.util.ArrayList;
import java.util.List;

public class Wire extends Component {

    private List<Component> connections;
    private List<Line> segments;
    private Point center;

    public Wire(String name, Point center) {
        super(name, center);
        this.connections = new ArrayList<>();
    }

    public void connect(Component component) {
        connections.add(component);
    }

    public void setConnections(List<Component> connections) {
        this.connections = connections;
    }

    public List<Component> getConnections() {
        return connections;
    }
}
