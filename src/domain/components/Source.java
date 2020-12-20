package domain.components;

import domain.geometry.Point;

import java.util.List;

public class Source extends Component implements IEnergizeable {

    private boolean on;

    public Source(String name, Point position, boolean on) {
        super(name, position);
        this.on = on;
    }

    public boolean isOn() {
        return on;
    }

    public void toggle() {
        on = !on;
    }

    @Override
    public List<Component> getAccessibleConnections() {
        if (isOn()) return this.getConnections();
        return List.of();
    }
}
