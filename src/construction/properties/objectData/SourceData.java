package construction.properties.objectData;

import construction.properties.Visitor;

public class SourceData extends ComponentData {

    public SourceData(String name) {
        super(name);
    }

    @Override
    public void accept(Visitor v) {
        v.setSourceMenu(this);
    }

    @Override
    public ObjectData applySettings(String name, boolean nameRight, boolean isClosed, String label, String subLabel, String acronym) {
        return new SourceData(name);
    }
}