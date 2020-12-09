package construction;

import application.canvas.PannableCanvas;
import domain.Grid;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ConstructionController {

    private Grid grid;

    private PannableCanvas canvas;

    public void initController(Grid grid, PannableCanvas canvas) {
        this.grid = grid;
        this.canvas = canvas;
    }

}
