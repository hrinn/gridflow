package domain.components;

import application.Globals;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
        createComponentIcon();
    }

    public Wire(Point p) {
        super("", p);
        this.connections = new ArrayList<>();
        start = p;
        end = p;
        energized = false;
        createComponentIcon();
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
        createComponentIcon();
    }

    @Override
    public List<Component> getConnections() {
        return connections;
    }

    @Override
    public ObjectNode getJSONObject(ObjectMapper mapper) {
        ObjectNode wire = super.getJSONObject(mapper);
        wire.put("start", start.toString());
        wire.put("end", end.toString());

        ArrayNode connections = mapper.createArrayNode();
        this.connections.forEach(c -> connections.add(c.getId().toString()));
        wire.put("connections", connections);

        return wire;
    }

    public void disconnect(UUID componentID) {
        connections.removeIf(connection -> connection.getId().equals(componentID));
        createComponentIcon();
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

    private void createComponentIcon() {
        WireIcon icon;
        if (isPointWire() && connections.size() > 1)
        {
            icon = ComponentIconCreator.getBlankWireIcon(start, end);
        } else {
            icon = ComponentIconCreator.getWireIcon(start, end);
        }
        icon.setWireIconEnergyState(false);
        icon.setComponentIconID(getId().toString());
        setComponentIcon(icon);
    }

    @Override
    public void updateComponentIcon() {
        WireIcon icon = (WireIcon) getComponentIcon();
        if (icon == null) return;
        icon.setWireIconEnergyState(energized);
    }
}
