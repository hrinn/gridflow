package construction.properties.objectData;

import construction.properties.Visitor;

public class BreakerData extends CloseableData {

    private String tandemID;

    public BreakerData(String name, boolean nameRight, boolean defaultState, String tandemID, double angle) {
        super(name, nameRight, defaultState, angle);
        this.tandemID = tandemID;
    }

    public String getTandemID() {
        return tandemID;
    }

    @Override
    public void accept(Visitor v) {
        v.setBreakerMenu(this);
    }

    @Override
    public ObjectData applySettings(String name, boolean nameRight, boolean isClosed, String label, String subLabel, String acronym) {
        return new BreakerData(name, nameRight, isClosed, tandemID, getAngle());
    }
}
