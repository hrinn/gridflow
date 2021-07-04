package construction.properties.objectData;

import construction.properties.Visitor;

// This class holds Component Data that the properties window displays and can set
public class ComponentData extends ObjectData {

    private String name;

    public ComponentData(String name) {
        this.name = name;
    }

    @Override
    public void accept(Visitor v) {
        v.setComponentMenu(this);
    }

    public String getName() {
        return name;
    }

    @Override
    public ObjectData applySettings(String name, boolean nameRight, boolean isClosed, String label, String subLabel, String acronym) {
        return new ComponentData(name);
    }
}
