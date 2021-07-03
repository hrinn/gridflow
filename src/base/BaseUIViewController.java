package base;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import security.Access;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class BaseUIViewController {

    public MenuBar TopMenuBar;
    private MenuFunctionController controller;
    private AboutPageController pageController;

    private Scene mainScene;

    private HostServices services;

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

    // Make width of menu bar scale to width of windo
    public void bindMenuBarWidthProperty(Stage primaryStage) {
        TopMenuBar.prefWidthProperty().bind(primaryStage.widthProperty());
    }

    // Loads the associated controlled
    public void setController(MenuFunctionController controller) {
        this.controller = controller;
    }

    public void setPageController(AboutPageController pageController) { this.pageController = pageController; }

    public void setMainScene(Scene scene) { this.mainScene = scene; }

    public void setServices(HostServices services) { this.services = services; }

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

//    @FXML
//    private void openAboutPage() throws IOException {
//        Stage dialog = new Stage();
//        FXMLLoader aboutPageLoader = new FXMLLoader(getClass().getResource("About.fxml"));
//        Parent AboutPage = aboutPageLoader.load();
//        setPageController(aboutPageLoader.getController());
//        this.pageController.setServices(this.services);
//
//        dialog.setScene(new Scene(AboutPage));
//        dialog.setTitle("About Gridflow");
//        dialog.initModality(Modality.APPLICATION_MODAL);
//        dialog.initOwner(this.mainScene.getWindow());
//        dialog.show();
//    }

    @FXML
    private void toggleFullscreen() {
        baseMenuFunctions.toggleFullscreen();
    }
}
