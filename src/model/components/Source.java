package model.components;

import model.geometry.Point;

import java.util.ArrayList;
import java.util.List;

public class Source extends Component implements IToggleable {

    //private Point position;
    private boolean on;
    private List<Wire> outputs;

    public Source(String name, Point position) {
        super(name, position);
        this.on = false;
        this.outputs = new ArrayList<>();
    }

    public void toggleState() {
        on = !on;
    }

    public boolean getState() {
        return on;
    }

    public int getOutputCount() {
        return outputs.size();
    }

    public void addOutput(Wire wire) {
        outputs.add(wire);
    }
}
