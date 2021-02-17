package construction;

import java.util.Objects;
import java.util.UUID;

public class PropertiesData {

    private ComponentType type;
    private UUID ID;
    private String name;
    private boolean defaultState;
    private double rotation;

    public PropertiesData() {
        this.type = null;
        this.ID = new UUID(0, 0);
        this.name = "";
        this.defaultState = true;
        this.rotation = 0;
    }

    public PropertiesData(ComponentType type, UUID ID, String name, boolean defState, double rot) {
        this.type = type;
        this.ID = ID;
        this.name = name;
        this.defaultState = defState;
        this.rotation = rot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PropertiesData)) return false;
        PropertiesData that = (PropertiesData) o;
        return defaultState == that.defaultState && Double.compare(that.rotation, rotation) == 0 && type == that.type && Objects.equals(ID, that.ID) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, ID, name, defaultState, rotation);
    }

    public ComponentType getType() { return type; }

    public void setType(ComponentType type) { this.type = type; }

    public UUID getID() { return ID; }

    public void setID(UUID ID) { this.ID = ID; }

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

    public void setDefaultState(boolean defaultState) { this.defaultState = defaultState; }

    public void setDefaultProperties(ComponentType type){
        setType(type);
        setID(new UUID(0, 0));
        setName("");
        setDefaultState(true);
        setRotation(0);
    }

    public void setDefaultComponentProperties(ComponentType type) {
        setType(type);
        setID(new UUID(0, 0));
        setDefaultState(true);
        setRotation(0);
    }

    public void copyContents(PropertiesData properties) {
        setType(properties.getType());
        setID(properties.getID());
        setName(properties.getName());
        setDefaultState(properties.getDefaultState());
        setRotation(properties.getRotation());
    }
}
