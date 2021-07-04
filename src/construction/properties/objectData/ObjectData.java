package construction.properties.objectData;

import construction.properties.Visitor;

// This ObjectData class is the parent class for AssociationData, ComponentData, and BreakerData
// This data is requested and displayed by the properties window, instead of actually getting entire objects
public abstract class ObjectData {
    public abstract void accept(Visitor v);

    // Creates a new ObjectData object with the updated settings
    public abstract ObjectData applySettings(String name, boolean nameRight, boolean isClosed, String label, String subLabel, String acronym);
}