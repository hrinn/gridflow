package base;

import application.events.GridChangedEvent;
import application.events.GridFlowEventManager;
import domain.Grid;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MenuFunctionController {

    private static final String DEFAULT_GRID_PATH = "./defaultgrid.json";
    private static final String DEFAULT_PATH = ".";
    private static final String DEFAULT_FILENAME = "grid";

    private GridFileManager gridFileManager;
    private GridFlowEventManager gridFlowEventManager;
    private String lastUsedFileName;
    private File lastUsedDirectory;
    private FutureTask<FileChooser> futureFC;

    public MenuFunctionController(GridFlowEventManager gridFlowEventManager) {
        this.gridFlowEventManager = gridFlowEventManager;
        this.gridFileManager = new GridFileManager();
        this.lastUsedFileName = DEFAULT_FILENAME;
        this.lastUsedDirectory = new File(DEFAULT_PATH);
        this.futureFC = new FutureTask<>(FileChooser::new);

        // Preload file dialog asynchronously
        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.execute(futureFC);
    }

    public Grid getGrid() {
        return gridFileManager.getGrid();
    }

    public void loadDefaultGrid() {
        gridFileManager.loadGrid(DEFAULT_GRID_PATH);
        gridFlowEventManager.sendEvent(new GridChangedEvent());
    }

    public void saveGrid(VBox menu) {
        // Get the main stage for dialog modality
        Window stage = menu.getScene().getWindow();

        try {
            // Retrieve the file chooser and configure
            FileChooser fc = futureFC.get();
            fc.setTitle("Save Grid File");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            fc.setInitialFileName(lastUsedFileName);
            fc.setInitialDirectory(lastUsedDirectory);

            // Show dialog
            File file = fc.showSaveDialog(stage);

            if (file == null){
                System.err.println("Save dialog canceled");
            } else {
                gridFileManager.saveGrid(file.getPath());
                lastUsedFileName = file.getName();
                lastUsedDirectory = file.getParentFile();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadGrid(VBox menu) {
        Window stage = menu.getScene().getWindow();

        try {
            FileChooser fc = futureFC.get();
            fc.setTitle("Load Grid File");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            fc.setInitialDirectory(lastUsedDirectory);

            File file = fc.showOpenDialog(stage);

            if (file == null){
                System.err.println("Load dialog canceled");
            } else {
                gridFileManager.loadGrid(file.getPath());
                gridFlowEventManager.sendEvent(new GridChangedEvent());
                lastUsedFileName = file.getName();
                lastUsedDirectory = file.getParentFile();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
