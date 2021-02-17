package construction.history;

import domain.components.Component;

import java.util.List;

public interface ComponentMemento {
    Component getComponent();
    List<String> getConnectionIDs();
}
