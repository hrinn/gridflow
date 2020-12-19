package domain.components;

import application.Globals;
import domain.geometry.*;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.WireIcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Wire extends Component {

    private List<Component> connections;
    private Point start;
    private Point end;
    private boolean energized;

    public Wire(Point p1, Point p2) {
        super("", Point.midpoint(p1, p2));
        this.connections = new ArrayList<>();
        start = p1;
        end = p2;
        energized = false;
        setDimensions();
    }

    public Wire(Point p) {
        super("", p);
        this.connections = new ArrayList<>();
        start = p;
        end = p;
        energized = false;
        setDimensions();
    }

    private void setDimensions() {
        this.getDimensions().setWidth(start.differenceX(end) / Globals.UNIT);
        this.getDimensions().setHeight(start.differenceY(end) / Globals.UNIT);
        this.getDimensions().setPadding(0.25);
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

    public void connect(Component... components) {
        connections.addAll(Arrays.asList(components));
    }

    public void setConnections(List<Component> connections) {
        this.connections = connections;
    }

    public List<Component> getConnections() {
        return connections;
    }

    public boolean isPointWire() {
        return start.equals(end);
    }

    public boolean isVerticalWire() {
        return start.getX() == end.getX() && start.getY() != end.getY();
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

        icon.setBoundingRect(getDimensions(), getPosition());
        return icon;
    }
}
