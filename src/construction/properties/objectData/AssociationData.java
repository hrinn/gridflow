package construction.properties.objectData;

import construction.properties.Visitor;

public class AssociationData extends ObjectData {

    private String label;
    private String subLabel;
    private String acronym;

    public AssociationData(String label, String subLabel, String acronym) {
        this.label = label;
        this.subLabel = subLabel;
        this.acronym = acronym;
    }

    @Override
    public void accept(Visitor v) {
        v.setAssociationMenu(this);
    }

    @Override
    public ObjectData applySettings(String name, boolean nameRight, boolean isClosed, String label, String subLabel, String acronym) {
        return new AssociationData(label, subLabel, acronym);
    }

    public String getLabel() {
        return label;
    }

    public String getSubLabel() {
        return subLabel;
    }

    public String getAcronym() {
        return acronym;
    }
}
