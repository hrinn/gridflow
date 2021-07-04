package construction.properties.objectData;

import construction.ComponentType;
import construction.properties.Visitor;

public class CloseableData extends ComponentData {
    private boolean defaultState;

    public CloseableData(ComponentType componentType, String name, boolean defaultState) {
        super(componentType, name);
        this.defaultState = defaultState;
    }

    public void accept(Visitor v) {
        v.setCloseableMenu(this);
    }

    public boolean isClosed() {
        return defaultState;
    }
}
