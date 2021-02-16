package construction.history;

import domain.Association;
import domain.components.Component;

import java.util.List;

public interface GridMemento {
    List<Component> getComponents();
    List<Association> getAssociations();
}
