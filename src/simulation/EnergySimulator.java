package simulation;

import application.events.Event;
import application.events.EventManager;
import application.events.IEventListener;
import model.Grid;
import model.components.Component;
import model.components.Source;
import model.components.Wire;

import java.util.*;

public class EnergySimulator implements IEventListener {

    private final Grid grid;
    private final EventManager eventManager;
    private final Map<UUID, Boolean> visitedMap;

    public EnergySimulator(Grid grid, EventManager eventManager) {
        this.grid = grid;
        this.eventManager = eventManager;
        eventManager.addListener(this);
        visitedMap = new HashMap<>();
    }

    public void handleEvent(Event event) {
        if (event == Event.GridChanged) {
            energyDFS();
        }
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
        eventManager.sendEvent(Event.GridEnergized);
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
