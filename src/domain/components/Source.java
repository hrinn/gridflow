package domain.components;

import domain.geometry.Point;

import java.util.List;

public abstract class Source extends Component implements IEnergizeable {

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
}
