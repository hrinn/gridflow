package domain.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import construction.ComponentType;
import construction.history.ComponentMemento;
import domain.geometry.*;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;
import visualization.componentIcons.WireIcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Wire extends Component {

    private List<Component> connections = new ArrayList<>();
    private Point start;
    private Point end;
    private boolean energized = false;
    private List<Point> bridgePoints = new ArrayList<>();

    public Wire(Point p1, Point p2) {
        super("", Point.midpoint(p1, p2));
        start = p1;
        end = p2;
        createComponentIcon();
    }

    public Wire(Point p) {
        super("", p);
        start = p;
        end = p;
        createComponentIcon();
    }

    public Wire(String id, String name, Point start, Point end, double angle, List<Point> bridgePoints, boolean energized, boolean nameRight) {
        super(UUID.fromString(id), name, Point.midpoint(start, end), angle, nameRight);
        this.bridgePoints = bridgePoints;
        this.energized = energized;
        this.start = start;
        this.end = end;
        createComponentIcon();
    }

    public Wire(JsonNode node, Point start, Point end) {
        super(UUID.fromString(node.get("id").asText()), node.get("name").asText(),
                Point.midpoint(start, end), node.get("angle").asDouble(), node.get("namepos").asBoolean());

        this.start = start;
        this.end = end;

        ArrayNode jsonBridgePoints = (ArrayNode)node.get("bridgePoints");
        if (jsonBridgePoints == null) return;
        jsonBridgePoints.forEach(jbp ->
                addBridgePoint(Point.fromString(jbp.asText()))
        );
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
    public void setConnections(List<Component> connections) {
        this.connections = connections;
        createComponentIcon();
    }

    @Override
    public ObjectNode getObjectNode(ObjectMapper mapper) {
        ObjectNode wire = super.getObjectNode(mapper);
        wire.remove("pos");
        wire.put("start", start.toString());
        wire.put("end", end.toString());

        ArrayNode connections = mapper.createArrayNode();
        this.connections.forEach(c -> connections.add(c.getId().toString()));
        wire.put("connections", connections);

        ArrayNode bridgePoints = mapper.createArrayNode();
        this.bridgePoints.forEach(bp -> bridgePoints.add(bp.toString()));
        wire.put("bridgePoints", bridgePoints);

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
        return start.differenceX(end) == 0;
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

    protected void createComponentIcon() {
        WireIcon icon;
        if (isPointWire() && connections.size() > 1)
        {
            icon = ComponentIconCreator.getBlankWireIcon(start, end);
        } else {
            icon = ComponentIconCreator.getWireIcon(start, end, bridgePoints);
        }
        icon.setWireIconEnergyState(false);
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName(), isNameRight());
        setComponentIcon(icon);
    }

    @Override
    public void updateComponentIcon() {
        WireIcon icon = (WireIcon) getComponentIcon();
        if (icon == null) return;
        icon.setWireIconEnergyState(energized);
    }

    @Override
    public void updateComponentIconName() {
        WireIcon icon = (WireIcon)getComponentIcon();
        icon.setComponentName(getName(), isNameRight());
    }

    @Override
    public ComponentType getComponentType() { return ComponentType.WIRE; }

    public void addBridgePoint(Point bridgePoint) {
        bridgePoints.add(bridgePoint);
        createComponentIcon();
    }

    public void removeBridgePoint(Point point) {
        for (Point bridgePoint : bridgePoints) {
            if (bridgePoint.equals(point)) {
                bridgePoints.remove(bridgePoint);
                createComponentIcon();
                return;
            }
        }
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    @Override
    public ComponentMemento makeSnapshot() {
        List<String> connectionIDs = connections.stream().map(connection -> connection.getId().toString()).collect(Collectors.toList());
        return new WireSnapshot(getId().toString(), getName(), start, end, bridgePoints, energized, connectionIDs, isNameRight());
    }
}

class WireSnapshot implements ComponentMemento {
    private String id;
    private String name;
    private Point start;
    private Point end;
    private List<Point> bridgePoints;
    private boolean energized;
    private List<String> connectionIDs;
    private boolean namepos;

    public WireSnapshot(String id, String name, Point start, Point end, List<Point> bps, boolean energized, List<String> connectionIDs, boolean namepos) {
        this.id = id;
        this.name = name;
        this.start = start.copy();
        this.end = end.copy();
        this.bridgePoints = new ArrayList<>();
        bps.forEach(bp -> bridgePoints.add(bp.copy()));
        this.energized = energized;
        this.connectionIDs = connectionIDs;
        this.namepos = namepos;
    }

    @Override
    public Component getComponent() {
        return new Wire(id, name, start, end, 0, bridgePoints, energized, namepos);
    }

    @Override
    public List<String> getConnectionIDs() {
        return connectionIDs;
    }
}
