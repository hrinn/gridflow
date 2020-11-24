package model.components;

import model.geometry.*;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.WireIcon;

import java.util.ArrayList;
import java.util.List;

public class Wire extends Component {

    private List<Component> connections;
    private Point start;
    private Point end;
    private boolean energized;

    public Wire(String name, Point p1, Point p2) {
        super(name, Point.midpoint(p1, p2));
        this.connections = new ArrayList<>();
        start = p1;
        end = p2;
        energized = false;
    }

    public Wire(String name, Point p) {
        super(name, p);
        this.connections = new ArrayList<>();
        start = p;
        end = p;
        energized = false;
    }

    public void energize() {
        energized = true;
    }

    public void deEnergize() {
        energized = false;
    }

    public boolean isEnergized() {
        return energized;
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

    @Override
    public List<Component> getAccessibleConnections() {
        energize();
        return connections;
    }

    @Override
    public ComponentIcon getComponentIcon() {
        WireIcon icon = ComponentIconCreator.getWireIcon(start, end);
        icon.setWireIconEnergyState(energized);
        icon.setComponentIconID(getId().toString());
        return icon;
    }
}
