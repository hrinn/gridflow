package construction.properties;

import construction.properties.objectData.AssociationData;
import construction.properties.objectData.BreakerData;
import construction.properties.objectData.CloseableData;
import construction.properties.objectData.ComponentData;

public interface Visitor {
    void setComponentMenu(ComponentData data);
    void setCloseableMenu(CloseableData data);
    void setBreakerMenu(BreakerData data);
    void setAssociationMenu(AssociationData data);
}
