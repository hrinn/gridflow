package construction;

public class PropertiesData {

    private ComponentType type;
    private String name;
    private boolean defaultState;
    private double rotation;

    public PropertiesData() {
        type = null;
        name = "";
        defaultState = false;
        rotation = 0;
    }

    public ComponentType getType() { return type; }

    public void setType(ComponentType type) { this.type = type; }

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

    public void setDefaultProperties(ComponentType type){
        setType(type);
        setName("");
        setDefaultState(true);
        setRotation(0);
    }
}
