package construction;

import domain.components.Voltage;

public class ComponentProperties {

    private String name;
    private boolean defaultState;
    private Voltage voltage;

    public ComponentProperties() {
        name = "";
        defaultState = false;
        voltage = Voltage.KV12;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getDefaultState() {
        return defaultState;
    }

    public void setDefaultState(boolean defaultState) {
        this.defaultState = defaultState;
    }

    public Voltage getVoltage() {
        return voltage;
    }

    public void setVoltage(Voltage voltage) {
        this.voltage = voltage;
    }
}
