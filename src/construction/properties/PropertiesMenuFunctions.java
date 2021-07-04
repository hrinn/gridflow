package construction.properties;

import construction.ComponentType;
import construction.properties.objectData.ObjectData;

public interface PropertiesMenuFunctions {
    ObjectData getObjectData(String objectID);
    ComponentType getComponentType(String objectID);
    void setObjectData(String objectID, ObjectData objectData);
}
