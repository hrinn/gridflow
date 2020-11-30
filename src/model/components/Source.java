package model.components;

import model.geometry.Point;

import java.util.ArrayList;
import java.util.List;

public class Source extends Component implements IEnergizeable {

    //private Point position;
    private boolean on;
    private List<Wire> outputs;

    public Source(String name, Point position, boolean on) {
        super(name, position);
        this.on = on;
        this.outputs = new ArrayList<>();
    }

    public int getOutputCount() {
        return outputs.size();
    }

    public void addOutput(Wire wire) {
        outputs.add(wire);
    }

    public boolean isOutputEnergized(int index) {
        return outputs.get(index).isEnergized();
    }

    @Override
    public List<Component> getAccessibleConnections() {
        if(on) {
            List<Component> outComponents = new ArrayList<>();
            for(Wire output : outputs) {
                outComponents.add(output);
            }
            return outComponents;
        }
        return List.of();
    }

    public boolean isOn() {
        return on;
    }

    public void toggle() {
        on = !on;
    }
}
