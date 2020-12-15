package simulation;

import application.events.Event;
import application.events.EventManager;
import application.events.IEventListener;
import domain.Grid;

public class SimulationController implements IEventListener {

    private final EnergySimulator model;
    private final EventManager eventManager;

    public SimulationController(Grid grid, EventManager eventManager) {
        this.model = new EnergySimulator(grid);
        this.eventManager = eventManager;
    }

    public void handleEvent(Event event) {
        if (event == Event.GridChanged) {
            model.energyDFS();
            eventManager.sendEvent(Event.GridEnergized);
        }
    }
}
