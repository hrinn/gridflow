package construction.properties;

public class PropertiesData {
    private boolean defaultState = true;
    private double rotation = 0;

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void toggleDefaultState() {
        defaultState = !defaultState;
    }

    public boolean getDefaultState() {
        return defaultState;
    }

    public void setDefaultState(boolean defaultState) {
        this.defaultState = defaultState;
    }
}
