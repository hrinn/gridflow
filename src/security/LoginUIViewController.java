package security;

import baseui.MenuFunctionController;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginUIViewController {

    public Button loginButton;
    public TextField user;
    public PasswordField pass;

    private MenuFunctionController controller;

    public void setController(MenuFunctionController controller) {
        this.controller = controller;
    }


}
