package construction.properties;

import construction.properties.objectData.*;

public interface Visitor {
    void setComponentMenu(ComponentData data);
    void setCloseableMenu(CloseableData data);
    void setBreakerMenu(BreakerData data);
    void setAssociationMenu(AssociationData data);
    void setSourceMenu(SourceData data);
}
