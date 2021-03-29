package security;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public class LoginUIViewController {

    public Button loginButton;
    public TextField user;
    public PasswordField pass;
    public StackPane LoginStackPane;

    private LoginController controller;

    public void initialize() {
    }

    public void setController(LoginController controller) {
        this.controller = controller;
    }

    @FXML
    private void tryLogin() {
        boolean res = controller.tryLogin(user.getText(), pass.getText());
        if (!res) {
            user.clear();
            pass.clear();
        }
    }
}
