package model.components.sources;

import model.components.Component;
import model.components.IToggleable;
import model.components.Wire;
import model.components.geometry.Point;

import java.util.List;

public class Source extends Component implements IToggleable {

    private Point position;
    private boolean on;
    private List<Wire> outputs;

    public Source(String name, Point position, List<Wire> outputs) {
        super(name);
        this.position = position;
        this.on = false;
        this.outputs = outputs;
    }

    public void toggleState() {
        on = !on;
    }

    public boolean getState() {
        return on;
    }
}
