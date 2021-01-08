package domain.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.geometry.Point;

import java.util.UUID;

public abstract class Closeable extends Device implements IToggleable {

    private boolean closed;
    private boolean closedByDefault;
    private boolean locked = false;

    public Closeable(String name, Point position, boolean closedByDefault) {
        super(name, position);
        this.closedByDefault = closedByDefault;
        closed = closedByDefault;
    }

    public Closeable(UUID id, String name, Point position, double angle, boolean closedByDefault, boolean closed) {
        super(id, name, position, angle);
        this.closedByDefault = closedByDefault;
        this.closed = closed;
    }

    public void toggleLocked() {
        locked = !locked;
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean isClosedByDefault() {
        return closedByDefault;
    }

    public void toggleClosed() {
        closed = !closed;
    }

    @Override
    public ObjectNode getObjectNode(ObjectMapper mapper) {
        ObjectNode closeable = super.getObjectNode(mapper);
        closeable.put("closed", closed);
        closeable.put("closedByDefault", closedByDefault);
        closeable.put("locked", locked);
        return closeable;
    }
}