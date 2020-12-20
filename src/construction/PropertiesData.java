package construction;

public class PropertiesData {

    private String name;
    private boolean defaultState;
    private double rotation;

    public PropertiesData() {
        name = "";
        defaultState = false;
        rotation = 0;
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

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setDefaultState(boolean defaultState) {
        this.defaultState = defaultState;
    }
}
