package construction;

import domain.components.Component;

// The BuildMenuViewController accesses data through this interface
public interface BuildMenuFunctions {
    // Changes the active tool
    void setToolType(ToolType toolType);

    // Changes the component being placed
    void setComponentType(Component componentType);
}
