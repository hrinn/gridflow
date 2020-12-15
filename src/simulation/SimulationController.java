package simulation;

import application.events.GridFlowEvent;
import application.events.GridFlowEventManager;
import application.events.GridFlowEventListener;
import domain.Grid;

public class SimulationController implements GridFlowEventListener {

    private final EnergySimulator model;
    private final GridFlowEventManager gridFlowEventManager;

    public SimulationController(Grid grid, GridFlowEventManager gridFlowEventManager) {
        this.model = new EnergySimulator(grid);
        this.gridFlowEventManager = gridFlowEventManager;
    }

    public void handleEvent(GridFlowEvent gridFlowEvent) {
        if (gridFlowEvent == gridFlowEvent.GridChanged) {
            model.energyDFS();
            gridFlowEventManager.sendEvent(gridFlowEvent.GridEnergized);
        }
    }
}
