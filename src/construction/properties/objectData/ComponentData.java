package construction.properties.objectData;

import construction.ComponentType;
import construction.properties.Visitor;

// This class holds Component Data that the properties window displays and can set
public class ComponentData extends ObjectData {

    private ComponentType componentType;
    private String name;

    public ComponentData(ComponentType componentType, String name) {
        this.componentType = componentType;
        this.name = name;
    }

    @Override
    public void accept(Visitor v) {
        v.setComponentMenu(this);
    }

    public String getName() {
        return name;
    }
}
