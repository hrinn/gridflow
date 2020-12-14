package construction;

public class ComponentProperties {

    private String name;
    private boolean defaultState;

    public ComponentProperties() {
        name = "";
        defaultState = false;
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
}
