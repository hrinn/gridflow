package security;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class LoginUIViewController {

    public Button loginButton;
    public TextField user;
    public PasswordField pass;

    private SecurityController controller;

    public void initialize() {
    }

    public void setController(SecurityController controller) {
        this.controller = controller;
    }

    @FXML
    private void tryLogin() {
        System.err.println("Trying login... view controller");
        boolean res = controller.tryLogin(user.getText(), pass.getText());
        if (!res) {
            user.clear();
            pass.clear();
        }
    }


}
