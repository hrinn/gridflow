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
}
