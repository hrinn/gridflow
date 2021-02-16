package simulation;

import application.events.*;
import domain.Grid;

public class SimulationController implements GridFlowEventListener {

    private final EnergySimulator model;
    private final GridFlowEventManager gridFlowEventManager;

    public SimulationController(Grid grid, GridFlowEventManager gridFlowEventManager) {
        this.model = new EnergySimulator(grid);
        this.gridFlowEventManager = gridFlowEventManager;
    }

    public void handleEvent(GridFlowEvent gridFlowEvent) {
        if (gridFlowEvent instanceof GridChangedEvent || gridFlowEvent instanceof StateRestoredEvent) {
            model.energyDFS();
            gridFlowEventManager.sendEvent(new GridEnergizedEvent());
        }
    }
}
