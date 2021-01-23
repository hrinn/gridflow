package baseui;

import application.DevUtils;
import application.events.GridChangedEvent;
import application.events.GridFlowEventManager;
import domain.Grid;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.awt.*;
import java.io.File;
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



    public void saveGrid(VBox menu) {
        FileChooser fc = new FileChooser();
        Window stage = menu.getScene().getWindow();

        fc.setTitle("Save Grid File");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fc.setInitialFileName("grid");

        try {
            File file = fc.showSaveDialog(stage);

            if (file == null){
                System.out.println("Could not save to file selected in dialog");
            } else {
                fc.setInitialDirectory(file.getParentFile());
                gridFileManager.saveGrid(file.getPath());
            }



        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadGrid() {
        gridFileManager.loadGrid(GRID_PATH);
        gridFlowEventManager.sendEvent(new GridChangedEvent());
    }
}
