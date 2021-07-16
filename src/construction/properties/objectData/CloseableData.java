package construction.properties.objectData;

import construction.properties.Visitor;

public class CloseableData extends ComponentData {
    private boolean defaultState;

    public CloseableData(String name, boolean nameRight, boolean defaultState, double angle) {
        super(name, nameRight, angle);
        this.defaultState = defaultState;
    }

    public void accept(Visitor v) {
        v.setCloseableMenu(this);
    }

    public boolean isClosed() {
        return defaultState;
    }

    @Override
    public ObjectData applySettings(String name, boolean nameRightOrTop, boolean isClosed, String label, String subLabel, String acronym) {
        return new CloseableData(name, getNamePos(nameRightOrTop), isClosed, getAngle());
    }
}
