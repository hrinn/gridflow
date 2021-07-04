package construction.properties.objectData;

import construction.ComponentType;
import construction.properties.Visitor;

public class BreakerData extends CloseableData {
    private String tandemID;

    public BreakerData(ComponentType componentType, String name, boolean defaultState, String tandemID) {
        super(componentType, name, defaultState);
        this.tandemID = tandemID;
    }

    @Override
    public void accept(Visitor v) {
        v.setBreakerMenu(this);
    }
}
