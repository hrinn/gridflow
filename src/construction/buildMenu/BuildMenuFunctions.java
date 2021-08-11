package construction.buildMenu;

import construction.ComponentType;
import construction.ToolType;

// The BuildMenuViewController accesses data through this interface
public interface BuildMenuFunctions {

    // Sets the current tool and component
    void setBuildMenuData(ToolType toolType, ComponentType componentType);

    void setBackgroundGridVisible(boolean state);

    void setPropertiesWindowVisible(boolean state);
}
