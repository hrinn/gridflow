package baseui;

import application.DevUtils;
import application.events.GridFlowEvent;
import application.events.GridFlowEventManager;
import domain.Grid;

public class MenuFunctionController {

    private GridFileManager gridFileManager;

    private GridFlowEventManager gridFlowEventManager;

    public MenuFunctionController(GridFlowEventManager gridFlowEventManager) {
        this.gridFlowEventManager = gridFlowEventManager;
        this.gridFileManager = new GridFileManager();
    }

    public Grid getGrid() {
        return gridFileManager.getGrid();
    }

    public void loadDefaultGrid() {
        gridFileManager.getGrid().loadComponents(DevUtils.createTestComponents());
        gridFlowEventManager.sendEvent(GridFlowEvent.GridChanged);
    }
}
