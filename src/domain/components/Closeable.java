package domain.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.geometry.Point;

public abstract class Closeable extends Device implements IToggleable {

    private boolean closed;
    private boolean closedByDefault;
    private boolean locked;

    public Closeable(String name, Point position, boolean closedByDefault) {
        super(name, position);
        this.closedByDefault = closedByDefault;
        closed = closedByDefault;
        locked = false;
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
    public ObjectNode getJSONObject(ObjectMapper mapper) {
        ObjectNode closeable = super.getJSONObject(mapper);
        closeable.put("closed", closed);
        closeable.put("closedByDefault", closedByDefault);
        closeable.put("locked", locked);
        return closeable;
    }
}
