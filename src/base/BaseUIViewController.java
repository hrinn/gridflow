package base;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;

public class BaseUIViewController {

    private MenuFunctionController controller;
    private BaseMenuFunctions baseMenuFunctions;

    // UI Items
    @FXML
    private VBox TopMenu;
    public MenuItem undoButton;
    public MenuItem redoButton;
    public MenuItem deleteButton;

    public void initialize() {
        // These set the keyboard shortcuts for menu items
        KeyCodeCombination ctrlZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.DOWN, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP);
        KeyCodeCombination ctrlShiftZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.ModifierValue.DOWN, KeyCombination.ModifierValue.DOWN, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP);
        KeyCodeCombination del = new KeyCodeCombination(KeyCode.DELETE, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP);

        undoButton.setAccelerator(ctrlZ);
        redoButton.setAccelerator(ctrlShiftZ);
        deleteButton.setAccelerator(del);
    }

    // Loads the associated controlled
    public void setController(MenuFunctionController controller) {
        this.controller = controller;
    }

    // Loads a controller that implements BaseMenuFunctions
    public void setBaseMenuFunctions(BaseMenuFunctions baseMenuFunctions) {
        this.baseMenuFunctions = baseMenuFunctions;
    }

    @FXML
    private void saveGrid() {
        controller.saveGrid(TopMenu);
    }

    @FXML
    private void loadGrid() { controller.loadGrid(TopMenu); }

    @FXML
    private void undo() {
        baseMenuFunctions.undo();
    }

    @FXML
    private void redo() {
        baseMenuFunctions.redo();
    }

    @FXML
    private void delete() {
        baseMenuFunctions.delete();
    }

    @FXML
    private void selectAll() {
        baseMenuFunctions.selectAll();
    }

    @FXML
    private void expandCanvas() {

    }

    @FXML
    private void zoomToFit() {
        baseMenuFunctions.zoomToFit();
    }
}
