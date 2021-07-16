package domain.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import construction.properties.objectData.CloseableData;
import construction.properties.objectData.ObjectData;
import domain.geometry.Point;

import java.util.UUID;

public abstract class Closeable extends Device implements IToggleable, ILockable {

    private boolean closed;
    private boolean closedByDefault;
    private boolean locked = false;

    public Closeable(String name, Point position, boolean closedByDefault) {
        super(name, position);
        this.closedByDefault = closedByDefault;
        closed = closedByDefault;
    }

    public Closeable(UUID id, String name, Point position, double angle, boolean closedByDefault, boolean closed, boolean locked) {
        super(id, name, position, angle);
        this.closedByDefault = closedByDefault;
        this.closed = closed;
        this.locked = locked;
    }

    public void toggleLocked() {
        locked = !locked;
    }

    public boolean isLocked() {
        return locked;
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

    public void setClosedByDefault(boolean closedByDefault) { this.closedByDefault = closedByDefault; }

    @Override
    public ObjectNode getObjectNode(ObjectMapper mapper) {
        ObjectNode closeable = super.getObjectNode(mapper);
        closeable.put("closed", closed);
        closeable.put("closedByDefault", closedByDefault);
        closeable.put("locked", locked);
        return closeable;
    }

    @Override
    public ObjectData getComponentObjectData() {
        return new CloseableData(getName(), isNameRight(), isClosedByDefault(), getAngle());
    }

    @Override
    public void applyComponentData(ObjectData objectData) {
        CloseableData data = (CloseableData) objectData;
        if (!getName().equals(data.getName()) || isNameRight() != data.isNamePos()) {
            setName(data.getName());
            setNameRight(data.isNamePos());
            updateComponentIconName();
        }
        if (this.closedByDefault != data.isClosed()) {
            this.closedByDefault = data.isClosed();
            createComponentIcon();
        }
    }
}
