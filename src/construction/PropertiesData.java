package construction;

import domain.components.Component;

import java.util.*;

public class PropertiesData {

    private ComponentType type;
    private UUID ID;
    private String name;
    private boolean defaultState;
    private double rotation;
    private int numSelected;
    private boolean namePosLeft;
    private boolean isAssociation;
    private String assocLabel;
    private String assocSubLabel;
    private String assocAcronym;
    private List<Component> tandemComponents;

    public PropertiesData() {
        this.type = null;
        this.ID = new UUID(0, 0);
        this.name = "";
        this.defaultState = true;
        this.rotation = 0;
        this.numSelected = 0;
        this.namePosLeft = false;
        this.isAssociation = false;
        this.assocLabel = "";
        this.assocSubLabel = "";
        this.assocAcronym = "";
        tandemComponents = new ArrayList<Component>(2);
    }

    public PropertiesData(ComponentType type, UUID ID, String name, boolean defState, double rot,
                          int numSelected, boolean namePosLeft, boolean isAssoc, String assocLabel,
                          String assocSubLabel, String assocAc) {
        this.type = type;
        this.ID = ID;
        this.name = name;
        this.defaultState = defState;
        this.rotation = rot;
        this.numSelected = numSelected;
        this.namePosLeft = namePosLeft;
        this.isAssociation = isAssoc;
        this.assocLabel = assocLabel;
        this.assocSubLabel = assocSubLabel;
        this.assocAcronym = assocAc;
        tandemComponents = new ArrayList<Component>(2);
    }

    public PropertiesData(ComponentType type, UUID ID, String name, boolean defState, double rot,
                          int numSelected, boolean namePosLeft, boolean isAssoc, String assocLabel,
                          String assocSubLabel, String assocAc, List<Component> tandemComps) {
        this.type = type;
        this.ID = ID;
        this.name = name;
        this.defaultState = defState;
        this.rotation = rot;
        this.numSelected = numSelected;
        this.namePosLeft = namePosLeft;
        this.isAssociation = isAssoc;
        this.assocLabel = assocLabel;
        this.assocSubLabel = assocSubLabel;
        this.assocAcronym = assocAc;
        this.tandemComponents = tandemComps;
    }

    public PropertiesData(PropertiesData PD) {
        this.type = PD.getType();
        this.ID = PD.getID();
        this.name = PD.getName();
        this.defaultState = PD.getDefaultState();
        this.rotation = PD.getRotation();
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


    // getters/setters

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

    public void setNumSelected (int num) { this.numSelected = num; }

    public int getNumSelected () { return this.numSelected; }

    public void setNamePos (boolean namePosLeft) { this.namePosLeft = namePosLeft; }

    public boolean getNamePos () { return this.namePosLeft; }

    public void setAssociation (boolean isAssoc) { this.isAssociation = isAssoc; }

    public boolean getAssociation () { return this.isAssociation; }

    public void setAssocLabel (String label) { this.assocLabel = label; }

    public String getAssocLabel () { return this.assocLabel; }

    public void setAssocSubLabel (String subLabel) { this.assocSubLabel = subLabel; }

    public String getAssocSubLabel () { return this.assocSubLabel; }

    public void setAssocAcronym (String acronym) { this.assocAcronym = acronym; }

    public String getAssocAcronym () { return this.assocAcronym; }

    public void addTandemComp (Component tandem) { this.tandemComponents.add(tandem); }

    public List<Component> getTandemComponents () { return this.tandemComponents; }

    public void setTandemList () { this.tandemComponents = new ArrayList<Component>(2); }

    public void setDefaultProperties(ComponentType type){
        setType(type);
        setID(new UUID(0, 0));
        setName("");
        setDefaultState(true);
        setRotation(0);
        setNumSelected(0);
        setNamePos(false);
        setAssociation(false);
        setAssocLabel("");
        setAssocSubLabel("");
        setAssocAcronym("");
        setTandemList();
    }

    public void setDefaultComponentProperties(int numSelected) {
        setType(null);
        setID(new UUID(0, 0));
        setName("");
        setDefaultState(true);
        setRotation(0);
        setNumSelected(numSelected);
        setNamePos(false);
        setAssociation(false);
        setAssocLabel("");
        setAssocSubLabel("");
        setAssocAcronym("");
        setTandemList();
    }

    public void copyContents(PropertiesData properties) {
        setType(properties.getType());
        setID(properties.getID());
        setName(properties.getName());
        setDefaultState(properties.getDefaultState());
        setRotation(properties.getRotation());
    }
}
