package base;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class BaseUIViewController {

    private MenuFunctionController controller;

    @FXML
    private VBox TopMenu;

    public void setController(MenuFunctionController controller) {
        this.controller = controller;
    }

    @FXML
    private void saveGrid() {
        controller.saveGrid(TopMenu);
    }

    @FXML
    private void loadGrid() { controller.loadGrid(TopMenu); }
}
