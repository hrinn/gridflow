package baseui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.String;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Translate;

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
    private void loadGrid() {
        controller.loadGrid();
    }
}
