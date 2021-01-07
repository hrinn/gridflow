package domain.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.geometry.Point;

import java.util.List;

public abstract class Source extends Component implements IToggleable {

    private boolean on;

    public Source(String name, Point position, boolean on) {
        super(name, position);
        this.on = on;
    }

    public boolean isOn() {
        return on;
    }

    protected void setOn(boolean on) {
        this.on = on;
    }

    public abstract void toggle();

    @Override
    public List<Component> getAccessibleConnections() {
        if (isOn()) return this.getConnections();
        return List.of();
    }

    @Override
    public ObjectNode getJSONObject(ObjectMapper mapper) {
        ObjectNode source = super.getJSONObject(mapper);
        source.put("on", on);
        return source;
    }
}
