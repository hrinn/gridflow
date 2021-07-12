package construction.properties;

import construction.ComponentType;
import construction.properties.objectData.ObjectData;

import java.util.List;

public interface PropertiesMenuFunctions {
    ObjectData getObjectData(String objectID);
    ComponentType getComponentType(String objectID);
    void setObjectData(String objectID, ObjectData objectData);
    void linkBreakers(List<String> breakerIDs);
    void clearTandem(String breakerID);
}
