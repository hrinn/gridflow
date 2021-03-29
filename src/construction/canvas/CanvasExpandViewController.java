package construction.canvas;

import application.Globals;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CanvasExpandViewController {

    private CanvasExpandController controller;

    // JavaFX Elements (linked to CanvasExpandView.fxml)
    public TextField widthTextBox;
    public TextField heightTextBox;

    public void setController(CanvasExpandController controller) {
        this.controller = controller;
    }

    public void setCurrentDimensions(double w, double h) {
        widthTextBox.setText("" + w);
        heightTextBox.setText("" + h);
    }

    // This is run when the user presses the apply button in the Expand Canvas window
    @FXML
    private void setCanvasSize() {
        try {
            double width = Double.parseDouble(widthTextBox.getText()) * Globals.UNIT;
            double height = Double.parseDouble(heightTextBox.getText()) * Globals.UNIT;
            controller.setCanvasSize(width, height);
            controller.closeAccountWindow();

        } catch (NumberFormatException e) {
            System.err.println("Invalid number");
            widthTextBox.clear();
            heightTextBox.clear();
        }
    }
}
