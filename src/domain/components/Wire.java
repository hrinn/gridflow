package domain.components;

import application.Globals;
import domain.geometry.*;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.WireIcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Override
    public List<Component> getConnections() {
        return connections;
    }

    public void disconnect(UUID componentID) {
        connections.removeIf(connection -> connection.getId().equals(componentID));
    }

    public boolean isPointWire() {
        return start.equals(end);
    }

    public boolean isVerticalWire() {
        return start.getX() == end.getX() && start.getY() != end.getY();
    }

    private List<Wire> getConnectedWires() {
        return connections.stream().filter(connection -> connection instanceof Wire)
                .map(connection -> (Wire)connection).collect(Collectors.toList());
    }

    @Override
    public void delete() {
        List<Wire> connectedWires = getConnectedWires();
        if (connections.size() > connectedWires.size()) {
            // there are connected non-wires, so the wire cannot be deleted
            throw new UnsupportedOperationException();
        }
        for (Wire connectedWire : connectedWires) {
            connectedWire.disconnect(getId());
        }
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
