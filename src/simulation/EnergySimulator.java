package simulation;

import model.Grid;
import model.components.Component;
import model.components.Source;

import java.util.*;

public class EnergySimulator {

    private Grid grid;
    private Map<UUID, Boolean> visitedMap;

    public EnergySimulator(Grid grid) {
        this.grid = grid;
    }

    public void EnergyDFS() {
        clearVisited();
        for (Source source : grid.getSources()) {
            explore(source);
        }
    }

    private void explore(Component component) {
        if (!visitedMap.get(component.getId())) {
            visitedMap.replace(component.getId(), true);
            component.getAccessibleConnections().stream()
                .forEach(connection -> explore(connection));
        }
    }


    private void clearVisited(){
        visitedMap = new HashMap<>();
        for (Component component : grid.getComponents()) {
            visitedMap.put(component.getId(), false);
        }
    }

}
