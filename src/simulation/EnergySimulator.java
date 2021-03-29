package simulation;

import domain.Grid;
import domain.components.Component;
import domain.components.Source;
import domain.components.Wire;

import java.util.*;

public class EnergySimulator {

    private final Grid grid;
    private final Map<UUID, Boolean> visitedMap;

    public EnergySimulator(Grid grid) {
        this.grid = grid;
        visitedMap = new HashMap<>();
    }

    private void deEnergizeGrid(){
        for (Wire wire : grid.getWires()) {
            wire.deEnergize();
        }
    }

    public void energyDFS() {
        deEnergizeGrid();
        clearVisited();
        for (Source source : grid.getSources()) {
            explore(source);
        }
    }

    private void explore(Component component) {
        if (!visitedMap.get(component.getId())) {
            visitedMap.replace(component.getId(), true);
            component.getAccessibleConnections().forEach(this::explore);
        }
    }

    private void clearVisited() {
        visitedMap.clear();
        for (Component component : grid.getComponents()) {
            visitedMap.put(component.getId(), false);
        }
    }
}
