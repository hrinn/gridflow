package construction.canvas;

import application.Globals;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CanvasExpandController {

    private CanvasExpandViewController viewController;
    private Scene scene;
    private Stage expandCanvasWindow;
    private GridCanvasFacade canvasFacade;

    private final static int WINDOW_WIDTH = 400;

    public CanvasExpandController(Scene scene, GridCanvasFacade canvasFacade) {
        this.scene = scene;
        this.canvasFacade = canvasFacade;

        try {
            initExpandWindow();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // Initializes the popup window
    private void initExpandWindow() throws IOException {
        Stage window = new Stage();
        FXMLLoader viewLoader = new FXMLLoader(getClass().getResource("CanvasExpandView.fxml"));
        Parent canvasExpandView = viewLoader.load();
        CanvasExpandViewController vc = viewLoader.getController();
        vc.setController(this);
        this.viewController = vc;

        window.setScene(new Scene(canvasExpandView));
        window.setTitle("Expand Canvas");
        window.initModality(Modality.APPLICATION_MODAL);
        window.initOwner(scene.getWindow());
        window.setMaxWidth(WINDOW_WIDTH);
        window.setMaxHeight(WINDOW_WIDTH);
        window.setMinHeight(WINDOW_WIDTH);
        window.setMinWidth(WINDOW_WIDTH);
        expandCanvasWindow = window;
    }

    // This is run when the expand canvas button is pressed in the Edit menu
    public void openAccountWindow() {
        double currentUnitWidth = canvasFacade.getCanvas().getPrefWidth() / Globals.UNIT;
        double currentUnitHeight = canvasFacade.getCanvas().getPrefHeight() / Globals.UNIT;
        viewController.setCurrentDimensions(currentUnitWidth, currentUnitHeight);

        expandCanvasWindow.show();
    }

    public void closeAccountWindow() {
        expandCanvasWindow.hide();
    }

    public void setCanvasSize(double w, double h) {

        canvasFacade.getCanvas().setPrefSize(w, h);
        canvasFacade.centerCanvas();
    }


}
