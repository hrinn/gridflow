package base;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import security.Access;

import java.awt.event.ActionEvent;

public class BaseUIViewController {

    private MenuFunctionController controller;

    // Most of the menu functions are handling by the Construction Controller
    // This interface gives this View Controller access to the Construction Controller's related functions
    private BaseMenuFunctions baseMenuFunctions;

    // UI Items
    @FXML
    private VBox TopMenu;
    public MenuItem undoButton;
    public MenuItem redoButton;
    public MenuItem deleteButton;
    public Menu EditMenu;
    public MenuItem clearButton;
    public MenuItem saveButton;
    public MenuItem manageAccountsButton;

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

    public void setPermissions(Access access) {
        if (access == Access.VIEWER) {
            EditMenu.setDisable(true); // viewers can't do any edits
            clearButton.setDisable(true); // viewers can't clear the grid
            saveButton.setDisable(true); // users can't save the grid
        }
        if (access != Access.GOD) {
            manageAccountsButton.setDisable(true);
        }
    }

    // These are the functions run when menu items are clicked

    @FXML
    private void manageAccounts() {
        baseMenuFunctions.openAccountManager();
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
    private void clearGrid() {
        selectAll();
        delete();
    }

    @FXML
    private void selectAll() {
        baseMenuFunctions.selectAll();
    }

    @FXML
    private void expandCanvas() {
        baseMenuFunctions.expandCanvas();
    }

    @FXML
    private void switchAccounts() {
        baseMenuFunctions.switchAccounts();
    }

    @FXML
    private void exit() {
        System.exit(0);
    }

    @FXML
    private void zoomToFit() {
        baseMenuFunctions.zoomToFit();
    }
}
