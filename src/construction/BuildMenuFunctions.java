package construction;

// The BuildMenuViewController accesses data through this interface
public interface BuildMenuFunctions {

    // Sets the current tool and component
    void setBuildMenuData(ToolType toolType, ComponentType componentType);
}
