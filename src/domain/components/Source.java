package domain.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.geometry.Point;

import java.util.List;
import java.util.UUID;

public abstract class Source extends Component implements IToggleable, ILockable {

    private boolean on;
    private boolean locked = false;

    public Source(String name, Point position, boolean on) {
        super(name, position);
        this.on = on;
    }

    public Source(UUID id, String name, Point position, double angle, boolean on) {
        super(id, name, position, angle);
        this.on = on;
    }

    public boolean isOn() {
        return on;
    }

    protected void setOn(boolean on) {
        this.on = on;
    }

    public abstract void toggleState();

    public void toggleLocked() {
        locked = !locked;
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public List<Component> getAccessibleConnections() {
        if (isOn()) return this.getConnections();
        return List.of();
    }

    @Override
    public ObjectNode getObjectNode(ObjectMapper mapper) {
        ObjectNode source = super.getObjectNode(mapper);
        source.put("on", on);
        return source;
    }
}
