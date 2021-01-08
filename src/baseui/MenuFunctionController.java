package baseui;

import application.DevUtils;
import application.events.GridChangedEvent;
import application.events.GridFlowEventManager;
import domain.Grid;

import java.io.IOException;

public class MenuFunctionController {

    private static final String GRID_PATH = "./grid.json";
    private static final String DEFAULT_GRID_PATH = "./defaultgrid.json";

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
        gridFileManager.loadGrid(DEFAULT_GRID_PATH);
        gridFlowEventManager.sendEvent(new GridChangedEvent());
    }

    public void saveGrid() {
        try {
            gridFileManager.saveGrid(GRID_PATH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadGrid() {
        gridFileManager.loadGrid(GRID_PATH);
        gridFlowEventManager.sendEvent(new GridChangedEvent());
    }
}
